Título del proyecto:
MedicalAppointmentsApp

Objetivo:
Desarrollar una aplicación Android para gestionar citas médicas. La aplicación tendrá diferentes tipos de usuarios (Administrador, Paciente, Doctor) y cada uno podrá interactuar con el sistema según sus permisos.

🔐 Tipos de Usuario:
Administrador

Puede gestionar pacientes, doctores y el sistema en general.

CRUD de usuarios (pacientes y doctores).

Visualiza y administra la lista de citas.

Paciente

Se registra e inicia sesión.

Visualiza doctores disponibles.

Solicita citas.

Consulta su historial de citas y estado.

Doctor

Visualiza sus citas asignadas.

Establece su horario disponible.

Cambia el estado de una cita (Ej: Atendida, Cancelada).

📦 Estructura sugerida de la base de datos (SQLite o Firebase):
Administrador

id, nombre, correo

Paciente

id, nombre, correo, telefono, historialMedico

Doctor

id, nombre, especialidad, horarioDisponible

Cita

idCita, fecha, hora, estado, idPaciente, idDoctor

Sistema (opcional como objeto lógico)

listaPacientes, listaDoctores, listaCitas

📱 Funcionalidades clave por pantalla:
1. Pantalla de Inicio / Login
Login con tipo de usuario

Registro de pacientes

2. Dashboard por tipo de usuario
Administrador: Ver listas, gestionar usuarios y citas

Paciente: Solicitar nueva cita, ver historial

Doctor: Ver sus citas, modificar estado, actualizar horario

3. Gestión de Citas
Formulario para crear/modificar cita

Calendario para seleccionar fecha y hora

Filtro por doctor o especialidad

4. Notificaciones
Confirmación de cita al paciente

Recordatorio al doctor

🔧 Tecnologías sugeridas:
Frontend: Java/Kotlin (Android Studio)

Backend (opcional): Firebase Realtime DB / Firestore

Login: Firebase Authentication

Persistencia: Room o Firebase

UI: Material Design Components

