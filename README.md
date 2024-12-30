
# **Utilizzo**
## Creare il jar:
```bash
cd rvm-robot
jar cvfm {name}.jar META-INF/MANIFEST.MF -C . .
```

## Eseguire il jar:
```bash
java --add-opens java.desktop/sun.awt=ALL-UNNAMED -jar {nome}.jar
```
