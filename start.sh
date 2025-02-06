#!/bin/bash

jar_name='jrobot.jar'
rm $jar_name 2>/dev/null
jar cvfm $jar_name META-INF/MANIFEST.MF -C . .
java --add-opens java.desktop/sun.awt=ALL-UNNAMED -jar $jar_name

