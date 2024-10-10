# Базовый образ для сборки Java приложения
FROM openjdk:17-jdk AS java_build

# Создаем рабочую директорию для приложения
RUN mkdir -p /opt/user/myapp

# Копируем библиотеку fastCGI.jar
COPY ./back/lib/fastCGI.jar /opt/user/myapp/lib/fastCGI.jar

# Копируем исходный код Java-сервера
COPY ./back/src/ /opt/user/myapp/src/

# Переходим в рабочую директорию
WORKDIR /opt/user/myapp

# Компилируем исходный код Java с зависимостью от fastCGI.jar
# Указываем classpath для компилятора Java с библиотекой
RUN javac -cp "/opt/user/myapp/src/:/opt/user/myapp/lib/fastCGI.jar" -d /opt/user/myapp/classes  /opt/user/myapp/src/Main.java


# Создаем манифест для JAR с указанием Class-Path для fastCGI.jar
RUN echo "Main-Class: Main" > manifest.txt
RUN echo "Class-Path: lib/fastCGI.jar" >> manifest.txt

# Упаковываем все классы и библиотеку в JAR-архив
RUN jar -cvfm myapp.jar manifest.txt -C classes/ . -C lib/ fastCGI.jar

# Базовый образ для сборки фронтенда
FROM node:14 AS frontend_build

# Переходим в директорию для фронтенда
WORKDIR /opt/user/myapp/frontend/

# Копируем исходный код фронтенда (TypeScript)
COPY ./front /opt/user/myapp/frontend/

# Устанавливаем зависимости для фронтенда
RUN npm install --only=dev

# Выполняем сборку фронтенда
RUN npm run build

# Финальный образ для запуска приложения
FROM openjdk:17-jdk-slim

# Копируем собранный JAR и фронтенд из предыдущих слоев
COPY --from=java_build /opt/user/myapp/myapp.jar /opt/user/myapp/myapp.jar
COPY --from=frontend_build /opt/user/myapp/frontend/dist /opt/user/myapp/frontend/dist
COPY --from=frontend_build /opt/user/myapp/frontend/ /opt/user/myapp/frontend/
# Копируем библиотеку FastCGI в финальный образ
COPY --from=java_build /opt/user/myapp/lib/fastCGI.jar /opt/user/myapp/lib/fastCGI.jar

# Указываем, что сервер будет слушать порт 9000
EXPOSE 9000

# Запуск Java-сервера с FastCGI на порту 9000
CMD ["java", "-DFCGI_PORT=9000", "-cp", "/opt/user/myapp/myapp.jar:/opt/user/myapp/lib/fastCGI.jar", "Main"]
