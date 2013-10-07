#!/bin/bash
rsync -az --progress ~/github/kurki13/src mkctammi@users.cs.helsinki.fi:/home/mkctammi/kurki13/
ssh -i id_rsa mkctammi@users.cs.helsinki.fi 'bash -s' < package_and_install.sh

