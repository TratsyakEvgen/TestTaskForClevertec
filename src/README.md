# Инструкция по запуску

1. Скачайте проект
2. Откройте командную строку и перейдите в каталог в который скачан проект (Например: cd C:\check)
3. Выполните команду: dir *.java /b /s > sources.txt (для Windows) или find -type f -name "*.java" > sources.txt (для
   Linux или macOS но это не точно)) )
4. Выполните команду: javac -d ./src @sources.txt
5. Запустите приложение командой: java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java с необходимыми
   арументами (Например: java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java 1-1 20-10 discountCard=1111
   balanceDebitCard=1000.00)

### PS:

В качестве задания 5 реализована собственный механизм внедрения зависимостей

