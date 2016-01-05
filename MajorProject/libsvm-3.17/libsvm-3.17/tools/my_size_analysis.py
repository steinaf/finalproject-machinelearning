#!/usr/bin/env python

import sys
import os
import glob
from subprocess import *

svmscale_exe = r"..\windows\svm-scale.exe"
svmtrain_exe = r"..\windows\svm-train.exe"
svmpredict_exe = r"..\windows\svm-predict.exe"
gnuplot_exe = r"C:\Program Files (x86)\gnuplot\bin\pgnuplot.exe"
grid_py = r".\grid.py"
easy_py = r".\easy.py"

test_pathname = "svmdata"
file_name = os.path.split(test_pathname)[1]
assert os.path.exists(test_pathname),"testing file not found"
scaled_test_file = file_name + ".scale"
predict_test_file = file_name + ".predict"
range_file = "svmdata.range"

for train_pathname in glob.glob(".\*.data"):
    print(train_pathname)
    file_name = os.path.split(train_pathname)[1]
    scaled_file = file_name + ".scale"
    model_file = file_name + ".model"

    cmd = '{0} -l 0 -r "{1}" "{2}" > "{3}"'.format(svmscale_exe, range_file, train_pathname, scaled_file)
    print('Scaling training data...')
    Popen(cmd, shell = True, stdout = PIPE).communicate()	

    cmd = '{0} -svmtrain "{1}" -gnuplot null -w1 4 -w-1 1 "{2}"'.format(grid_py, svmtrain_exe, scaled_file)
    print('Cross validation...')
    f = Popen(cmd, shell = True, stdout = PIPE).stdout

    line = ''
    while True:
        last_line = line
        line = f.readline()
        if not line: break
    c,g,rate = map(float,last_line.split())

    print('Best c={0}, g={1} CV rate={2}'.format(c,g,rate))

    cmd = '{0} -c {1} -g {2} "{3}" "{4}"'.format(svmtrain_exe,c,g,scaled_file,model_file)
    print('Training...')
    Popen(cmd, shell = True, stdout = PIPE).communicate()

    cmd = '{0} "{1}" "{2}" "{3}"'.format(svmpredict_exe, scaled_test_file, model_file, predict_test_file)
    print('Testing...')
    Popen(cmd, shell = True).communicate()	

    print('Output prediction: {0}'.format(predict_test_file))
