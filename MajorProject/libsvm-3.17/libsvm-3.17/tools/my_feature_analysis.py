#!/usr/bin/env python

import sys
import os
import glob
from subprocess import *

# svm, grid, and gnuplot executable files

is_win32 = (sys.platform == 'win32')
if not is_win32:
	svmscale_exe = "../svm-scale"
	svmtrain_exe = "../svm-train"
	svmpredict_exe = "../svm-predict"
	grid_py = "./grid.py"
	gnuplot_exe = "/usr/bin/gnuplot"
else:
        # example for windows
	svmscale_exe = r"..\windows\svm-scale.exe"
	svmtrain_exe = r"..\windows\svm-train.exe"
	svmpredict_exe = r"..\windows\svm-predict.exe"
	gnuplot_exe = r"C:\Program Files (x86)\gnuplot\bin\pgnuplot.exe"
	grid_py = r".\grid.py"

assert os.path.exists(svmscale_exe),"svm-scale executable not found"
assert os.path.exists(svmtrain_exe),"svm-train executable not found"
assert os.path.exists(svmpredict_exe),"svm-predict executable not found"
assert os.path.exists(gnuplot_exe),"gnuplot executable not found"
assert os.path.exists(grid_py),"grid.py not found"

for train_pathname in glob.glob(".\*.data"):
    assert os.path.exists(train_pathname),"training file not found"
    file_name = os.path.split(train_pathname)[1]
    scaled_file = file_name + ".scale"
    model_file = file_name + ".model"
    range_file = file_name + ".range"

    cmd = '{0} -l 0 -s "{1}" "{2}" > "{3}"'.format(svmscale_exe, range_file, train_pathname, scaled_file)
    print('Scaling training data...')
    Popen(cmd, shell = True, stdout = PIPE).communicate()	

    cmd = '{0} -svmtrain "{1}" -gnuplot "null" -w1 4 -w-1 1 -m 200 "{3}"'.format(grid_py, svmtrain_exe, gnuplot_exe, scaled_file)
    print('Cross validation... '+train_pathname)
    f = Popen(cmd, shell = True, stdout = PIPE).stdout

    line = ''
    while True:
        last_line = line
        line = f.readline()
        if not line: break
    c,g,rate = map(float,last_line.split())

    f2 = open('output', 'a')
    f2.write('{0} Best c={1}, g={2} CV rate={3}\n'.format(train_pathname,c,g,rate))
        
