# 🚀 MyGarageApp: Gestión de Revisiones de Vehículos

## 📌 Descripción
MyGarageApp es una aplicación Android diseñada para ayudar a los usuarios a **gestionar las revisiones de sus vehículos, encontrar talleres cercanos y realizar consultas interactivas** sobre mantenimiento y mecánica.  

## 🎯 Características Principales
- **📅 Gestión de Revisiones:** Guarda el historial de revisiones y programa y recibe notificaciones sobre mantenimiento de tus vehículos.
- **🛠️ Búsqueda de Talleres Cercanos:** Encuentra talleres cercanos a tu localizacion y consulta su informacion y reseñas.
- **💬 Consultas Interactivas:** Pregunta sobre mecánica y revisiones a través de un sistema de consulta interactiva y personalizada.

## 🏗️ Tecnologías Utilizadas
- **Kotlin** y **Jetpack Compose** para el desarrollo de la interfaz.
- **WorkManager** para ejecutar tareas en segundo plano, como recordatorios de revisiones.
- **Firebase Authentication** para el acceso seguro de usuarios.
- **Firestore** como copia de seguridad en la nube
- **Google Maps API** para mostrar ubicaciones de talleres cercanos.
- **Hilt** para la gestión de dependencias.
- **Room Database** para almacenar datos de los vehículos y sus mantenimientos.

## ⚙️ Instalación y Configuración
1. **Clona el repositorio:**  
   ```sh
   git clone [https://github.com/tuusuario/MyGarageApp.git](https://github.com/cargarrod12/MyGarageApp)
   
## 2. 📱 Abre el proyecto en Android Studio

1. Instala **Android Studio**.
2. Abre **Android Studio**.
3. Selecciona **"Open an existing project"** desde el menú inicial.
4. Navega hasta la carpeta donde clonaste el repositorio.
5. Selecciona la carpeta raíz del proyecto.
6. Espera a que Gradle sincronice correctamente todas las dependencias.

> 💡 Asegúrate de tener instalada una versión compatible del SDK de Android (mínimo API 24) y habilitados los complementos de Kotlin, Jetpack Compose y Google Maps.

## 3. ⚙️ Configura las claves necesarias

Antes de ejecutar la aplicación, es necesario configurar las claves de API que utiliza el proyecto:

### 🤖 Clave de OpenAI (ChatGPT)
Para usar la funcionalidad de consejos personalizados mediante IA:

1. Añade al archivo llamado `gradle.properties` en la carpeta raíz del proyecto.
2. Añade esta línea con tu clave: OPENAI_API_KEY=TU_CLAVE_API

Puedes obtener una clave desde: https://platform.openai.com/account/api-keys


## 4. ▶️ Ejecutar la aplicación

Una vez configurado todo correctamente, puedes ejecutar la aplicación siguiendo estos pasos:

1. Conecta un dispositivo Android físico o utiliza un emulador configurado.
2. En Android Studio, selecciona el módulo `app` en la parte superior del entorno.
3. Haz clic en el botón de **Run** (▶️) o presiona **Shift + F10**.
4. La aplicación se compilará y se instalará en el dispositivo seleccionado.

> 🧪 Asegúrate de tener conexión a internet si deseas probar funcionalidades como los mapas o la generación de consejos personalizados mediante OpenAI.


## 6. ✨ Funcionalidades principales

Esta aplicación Android permite al usuario gestionar de forma eficiente el mantenimiento y las revisiones de sus vehículos. A continuación se detallan las funcionalidades clave que ofrece:

### 🛠️ Módulo de revisiones
- Registro de múltiples vehículos por usuario.
- Visualización de información relevante como kilometraje, imagen y revisiones asociadas.
- Edición de datos del vehículo (imagen, estado, kilómetros).
- Visualización de revisiones próximas.
- Historial de revisiones realizadas con opción de añadir nuevas entradas.
- Recordatorios inteligentes de mantenimiento.
- Notificaciones configurables por vehículo.

### 🗺️ Módulo de mapas
- Muestra talleres cercanos según la ubicación del dispositivo.
- Visualización de datos del taller: nombre, dirección, horario, valoraciones.
- Acceso directo a las reseñas.

### 🤖 Módulo de consejos con IA
- Consulta interactiva de dudas sobre revisiones o mantenimiento.
- Respuestas personalizadas según el vehículo seleccionado.
- Uso de la API de OpenAI para generar contenido relevante en tiempo real.



