#!/usr/bin/env python3

import json
import yaml
from yaml import Loader
import os
import requests
import argparse
import subprocess

#
#parser = argparse.ArgumentParser(description='Run dextorm experiment')
#parser.add_argument('dextorm_jar_path', help="the path to the dextorm executable",default=)
#parser.add_argument('dextorm_conf_path', help="the path to the dextorm configuration file")
#parser.add_argument('jacoco_report_xml', help="the path to the jacoco report xml file")
#args = parser.parse_args()
class Args:
    def __init__(self):
        self.dextorm_jar_path="/home/nherbaut/workspace/dextorm/DEXTORM/target/dextorm.jar"
        self.dextorm_conf_path="/home/nherbaut/workspace/dextorm/DEXTORM/src/main/resources/dextorm.yaml"
        self.jacoco_report_xml="/home/nherbaut/workspace/dextorm/dextorm-dummy-project/target/site/jacoco-ut/jacoco.xml"
    
args=Args()

with open(args.dextorm_conf_path) as f:
    conf=yaml.load(f,Loader=Loader)


for method in conf["differs"].keys():
    conf["app"]["diffAlgorithm"]=method
    with open("dextorm.yaml","w") as ff:
        yaml.dump(conf, ff)
    print(f"running with {method}") 
    subprocess.run(["java","-jar", args.dextorm_jar_path, "dextorm.yaml", args.jacoco_report_xml])
    
data={}
for dirpath, dirnames, filenames in os.walk("."):
    for filename in [fn for fn in filenames if fn.endswith(".json")]:
        with open(filename) as f:
            json_content=json.load(f)
            if (items := list(json_content.items()))[0][0] not in data:
                data.update(json_content)
            else:
                data[items[0][0]].update(items[0][1])

for k,v in conf["publishers"]["restPublishers"].items():
    requests.post(v["baseUrl"],json=data,headers={"Content-type":"application/json"})
            