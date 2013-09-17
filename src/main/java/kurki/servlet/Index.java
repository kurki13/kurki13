package kurki.servlet;

import kurki.*;
import service.*;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.velocity.*;
import org.apache.velocity.context.*;
import org.apache.velocity.servlet.*;

public class Index extends VelocityServlet implements Log, Serializable {

    public static final String RESULT = "result";
    public static final String ERROR = "error";
    public static final String INDEX = "index";
    public static final String SERVICE = "service";
    public static final String COURSE = "course";
    private boolean initialized = false;
    private final String BASIC_TEMPLATE = "index.vm";
    private final String SYNCHRONIZATION_ERROR = "syncerror.vm";
    public static final String TIMESTAMP = "TS";
    private int session_lenght = 3600;
    public static final String KURKI_SESSION = "session";
    public static final Hashtable handlers = new Hashtable();

    static {
        Session.initialize();
    }

    @Override
    public synchronized void init() {
        if (!initialized) {
            try {
                init_config();
                init_handlers();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
            initialized = true;
        }
    }

    //MKCT: Wtf do we need handlers for when we have the ServiceManager singleton?
    private void init_handlers() {
        ServiceManager serviceManager = Session.getServiceManager();
        handlers.put(ServiceName.ENTRY,
                new Entry(serviceManager.getService(ServiceName.ENTRY)));
        handlers.put(ServiceName.PARTICIPANTS,
                new Participants(serviceManager.getService(ServiceName.PARTICIPANTS)));
        handlers.put(ServiceName.COURSE_BASICS,
                new CourseBasics(serviceManager.getService(ServiceName.COURSE_BASICS)));
        handlers.put(ServiceName.CHECKLIST,
                new Checklist(serviceManager.getService(ServiceName.CHECKLIST)));
        handlers.put(ServiceName.GRADES,
                new Grades(serviceManager.getService(ServiceName.GRADES)));
        handlers.put(ServiceName.RESULT_LIST,
                new ResultList(serviceManager.getService(ServiceName.RESULT_LIST)));
        handlers.put(ServiceName.FREEZE,
                new Freeze(serviceManager.getService(ServiceName.FREEZE)));
    }

    private void init_config() {
        ServletContext ctx = getServletContext();
        Configuration.setPropertiesFile(new File(ctx.getRealPath(ctx.getInitParameter("configurationFile"))));
        //Lisätään tieto lokista konfiguraatioon.
        Configuration.setProperty("log", this);
        if (Configuration.propertySet("superUsers")) {
            Session.setSuperUsers((String) Configuration.getProperty("superUsers"));
        }
    }

    @Override
    protected void doRequest(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse)
            throws ServletException, IOException {

        Vector vec = new Vector();
        String timestamp;
        String error = "";
        String result = "";
        CourseInfo course;
        Session session = null;
        Context context = null;
        AbstractVelocityServiceProvider serviceProvider;
        Template template = null;
        HttpSession httpSession = null;

        try {
            // Luodaan template-konteksti 
            context = createContext(servletRequest, servletResponse);
            //lokalisaatiobundlen lisääminen kontekstiin
            context.put("bundle", LocalisationBundle.getBundle());

            setContentType(servletRequest, servletResponse);
            /*
             *  Onko kyseessä jo joskus aloitettu, kenties vanhentunut sessio.
             */
            boolean oldSession = servletRequest.getRequestedSessionId() != null;

            /*
             *  Hae voimassaoleva sessio tai aloita uusi
             */
            httpSession = servletRequest.getSession();
            httpSession.setMaxInactiveInterval(session_lenght);

            // Ei käsitellä kahta saman session http-pyyntöä yhtä aikaa
            if (!ServletMonitor.getMonitor().lock(httpSession)) {
                httpSession = servletRequest.getSession(true);
                ServletMonitor.getMonitor().lock(httpSession);
            }

            if (oldSession && httpSession.isNew()) {
                result += LocalisationBundle.getString("istuntoVanhentunutError");
            }

            Object tmpSession = httpSession.getAttribute(KURKI_SESSION);

            if (tmpSession != null) {
                session = (Session) tmpSession;
            } else {
                session = Session.getInstance(servletRequest.getRemoteUser());
                httpSession.setAttribute(KURKI_SESSION, session);
            }

            /*
             *  Millä kursseilla käyttäjä on ohjaajana tai luennoitsijana
             *  --> courses
             */
            CourseInfo[] courseInfos = session.getCourseInfos();
            context.put("courseInfos", (courseInfos.length > 0 ? courseInfos : null));


            /*
             *  Selvitä valittu kurssi
             */
            try {
                // Onko kurssivalinta tullut http-parametrina?
                course = session.getCourseInfo(nullIfEmpty(servletRequest.getParameter(COURSE)));

            } catch (Exception e) {
                course = null;
            }

            // kurssi http-parametrina
            if (course != null) {
                session.setSelectedCourse(course);
            } // kurssi valittu aikaisemmin
            else if (session.courseSelected()) {
                course = session.getSelectedCourseInfo();
            }

            context.put("selectedCourse", session.getSelectedCourse());

            /*
             *  Selvitä mihin toimintoihin käyttäjä on oikeutettu 
             *  suhteessa valittuun kurssiin
             */
            if (session.courseSelected()) {
                context.put("services",
                        session.getValidServices());
            }


            /*
             *  Pyytääkö käyttäjä uutta palvelua?
             */
            String service = nullIfEmpty(servletRequest.getParameter(SERVICE));

            if (service != null
                    && session.courseSelected()) {

                // palvelua ei ole määritelty tai käyttäjän oikeudet eivät riitä
                if (!session.setService(service)) {
                }
            }

            /*
             *  Aikaleiman tarkistus
             */
            String reqts = servletRequest.getParameter(TIMESTAMP);
            Object tmp = httpSession.getAttribute(TIMESTAMP);
            if (tmp != null) {
                timestamp = (String) tmp;
            } else {
                timestamp = null;
            }

            /*
             *  Palvelin ja käyttöliittymä eivät ole synkronissa - kriittinen toiminto
             */
            if (reqts != null && timestamp != null && !reqts.equals(timestamp)) {
                template = getTemplate(SYNCHRONIZATION_ERROR);
            } 
            //Kurssi ja palvelu valittu
            else if (session.courseSelected()
                    && session.serviceSelected()) {

                // 
                serviceProvider =
                        (AbstractVelocityServiceProvider) getHandlerFor(session.getSelectedService());

                context.put("selectedService", serviceProvider);

                String serviceTemplateName = serviceProvider.handleRequest(session, servletRequest, servletResponse, context);

                if (serviceTemplateName != null) {
                    template = getTemplate(serviceTemplateName);
                }
            } // muuten pyydä valitsemaan palvelu
            else {
                template = getTemplate(BASIC_TEMPLATE);
            }

            try {
                /*
                 *  Uusi aikaleima.
                 */
                timestamp = "" + System.currentTimeMillis();
                httpSession.setAttribute(TIMESTAMP, timestamp);
                context.put(TIMESTAMP, timestamp);

                /*
                 *  Automaattinen välitallennus - päällä vai poissa?
                 */
                tmp = nullIfEmpty(servletRequest.getParameter("asToggle"));
                if (tmp != null) {
                    context.put("asToggle", tmp);
                    httpSession.setAttribute("asToggle", tmp);

                } else if ((tmp = httpSession.getAttribute("asToggle")) != null) {
                    context.put("asToggle", tmp);
                }

                context.put("sessionLen", "" + session_lenght);
            } catch (IllegalStateException ise) {
            }


            context.put("webmaster", (String) Configuration.getProperty("webmaster"));

            if (nullIfEmpty(error) != null) {
                context.put(Index.ERROR, error);
            }
            if (nullIfEmpty(result) != null) {
                context.put(Index.RESULT, result);
            }

            /*
             *  bail if we can't find the template
             */
            if (template == null) {
                return;
            }

            /*
             *  now merge it
             */

            mergeTemplate(template, context, servletResponse);

            /*
             *  call cleanup routine to let a derived class do some cleanup
             */

            requestCleanup(servletRequest, servletResponse, context);
        } catch (Exception e) {
            outputError(e, servletResponse);
        } finally {
            if (httpSession != null) {
                ServletMonitor.getMonitor().unlock(httpSession);
            } else {
                ServletMonitor.getMonitor().notifyAll();
            }
        }
    }

    private void outputError(Exception e, ServletResponse servletResponse) throws IOException {

        /*
         *  call the error handler to let the derived class
         *  do something useful with this failure.
         */
        ServletOutputStream out;
        servletResponse.setContentType("text/html");
        out = servletResponse.getOutputStream();

        out.println("<html><head>\n<title>Kurki: virheilmoitus</title>\n"
                + "<link rel='stylesheet' href='../kurki.css' title='kurki'>\n</head><body>\n"
                + "<div class='error' style='text-align:center;width=500px'>\n"
                + "<h2>Virheilmoitus</h2>\n<hr>\n<pre align='left'>\n");
        e.printStackTrace(new PrintStream(out));
        out.println("\n</pre>\n<hr>\n<a href=\"mailto:tktl-kurki@cs.Helsinki.FI\">tktl-kurki@cs.Helsinki.FI</a></div></body></html>");

        e.printStackTrace();
    }

    protected String nullIfEmpty(String str) {
        if (str == null || str.equals("")) {
            return null;
        } else {
            return str;
        }
    }

    public AbstractVelocityServiceProvider getHandlerFor(Service service) {
        return (AbstractVelocityServiceProvider) handlers.get(service.getId());
    }

    public static String asNotify(String target) {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        return target + LocalisationBundle.getString("tallennettuAutom")
                + calendar.get(Calendar.DAY_OF_MONTH)
                + "." + (calendar.get(Calendar.MONTH) + 1)
                + "." + calendar.get(Calendar.YEAR) + " klo "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + (minute < 10 ? "0" + minute : "" + minute) + ".";
    }
}
