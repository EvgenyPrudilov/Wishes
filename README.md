# Wishes Service
Платформа управления списками желаний (вишлистами) и подарками, построенный на базе архитектуры Spring. Она отвечает за создание персональных вишлистов, управление добавленными подарками, их ценой, ссылками, а также гибкую настройку приватности (публичный доступ, доступ для друзей) и механизмы бронирования подарков без раскрытия сюрприза для владельца.

## Основной функционал
- **Управление вишлистами**: создание списков, привязка к дате события (например, день рождения).
- **Каталог подарков**: добавление позиций с ценами, текстовым описанием, изображениями и ссылками на магазины.
- **Настройки приватности**: управление уровнями доступа к спискам.
- **Система бронирования**: возможность анонимного или авторизованного резервирования подарков друзьями.

## Технологический стек
- **Backend:** Java 21, Spring Boot 4.x, Spring Cloud Config, Spring Security
- **Data Access:** Spring Data JPA, Hibernate, PostgreSQL
- **Infrastructure:** Docker, Docker Compose, Nginx Gateway
- **Utilities:** Lombok, Mapper

## Быстрый запуск (Docker Compose)

### 1. Переменные окружения
Убедитесь, что в корне проекта созданы `.env` файлы (например, `shared.env`, `profile.env`), содержащие параметры доступа:
```env
POSTGRES_USER=cohenrol
POSTGRES_PASSWORD=12345678
PROFILE_DB_EXTERNAL_PORT=8301
```

### 2. Генерация Keystore
Для работы сервиса авторизации (подпись JWT) необходимо локально сгенерировать файл `keystore.p12`.

1. Выполните команду в корневом каталоге проекта (замените `YOUR_SECRET_PASSWORD` на ваш пароль):
```bash
keytool -genkeypair \
  -alias AUTHENTICATION_SERVICE_JWT_KEYSTORE_ALIAS \
  -keyalg EC \
  -groupname secp256r1 \
  -validity 365 \
  -keystore keystore.p12 \
  -storetype PKCS12 \
  -storepass AUTHENTICATION_SERVICE_JWT_KEYSTORE_PASSWORD \
  -dname "CN=auth-server, OU=Development, O=Cohenrol, C=NL" \
  -noprompt
```

2. Добавьте использованный пароль в файл конфигурацию в .security `auth.env`  с именем алиаса и расположением его:
```env
AUTHENTICATION_SERVICE_JWT_KEYSTORE_PASSWORD=YOUR_SECRET_PASSWORD
AUTHENTICATION_SERVICE_JWT_KEYSTORE_ALIAS=auth-server-ec
AUTHENTICATION_SERVICE_JWT_KEYSTORE_LOCATION=file:/app/resources/keystore.p12
```
Также необходимо установить имя и пароль для баз данных в файлах в .security:
```env
POSTGRES_USER=YOUR_NAME
POSTGRES_PASSWORD=YOUR_SECRET_PASSWORD
```

### 3. Запуск инфраструктуры
Запустите сборку и старт всех связанных контейнеров (включая СУБД, Nginx шлюз и Config-сервер):
```bash
docker compose -f docker-compose-main.yml up --build
```

### 4. Проверка работоспособности


