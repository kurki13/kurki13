#!/bin/bash
rsync -az --progress ~/github/kurki13 mkctammi@users.cs.helsinki.fi:/home/mkctammi/
ssh -i id_rsa mkctammi@users.cs.helsinki.fi 'bash -s' < package_and_install.sh

