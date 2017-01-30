#!/usr/bin/python

import sys
from subprocess import call
from xml.etree import ElementTree as et

if (len(sys.argv) < 2):
	print "Needs a version"
	exit(1)

version = sys.argv[1]
xml_path = "releng/br.usp.each.saeg.baduino.configuration/pom.xml"

print "Updating config pom"
nsmap = {"p": "http://maven.apache.org/POM/4.0.0"}
et.register_namespace("", "http://maven.apache.org/POM/4.0.0")
tree = et.parse(xml_path)
root = tree.getroot()
version_element = root.findall(".//p:artifact[@id='baduino']/p:version", nsmap)[0]
version_element.text = version

print "Writing new pom"
tree.write(xml_path)

print "Updating remaining pom in project"
call("mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=%s -Dtycho.mode=maven" % version, shell=True)