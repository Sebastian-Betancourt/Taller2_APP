# ExpenseTracker

ExpenseTracker es una aplicación móvil desarrollada en **Android con Kotlin y Jetpack Compose**, que permite a los usuarios registrar, administrar y controlar sus gastos personales de manera sencilla y visual. La aplicación incluye funcionalidades de registro de gastos, categorización, historial, cálculo del total gastado y recordatorios diarios.

---

## Funcionalidades principales

1. **Registro de gastos**
   - Permite ingresar un nuevo gasto con los siguientes campos:
     - **Monto**: cantidad del gasto (números decimales permitidos).
     - **Descripción**: detalle de la transacción.
     - **Categoría**: selección de una categoría predefinida (Ej. Comida, Transporte, Entretenimiento).

2. **Historial de gastos**
   - Muestra una lista de todos los gastos registrados.
   - Cada gasto muestra:
     - Descripción
     - Categoría
     - Fecha y hora de registro
     - Monto gastado
   - Posibilidad de **editar** o **eliminar** cualquier gasto directamente desde la lista.

3. **Edición de gastos**
   - Permite modificar:
     - Monto
     - Descripción
     - Categoría
   - Se accede mediante el icono de **editar** en cada item de gasto.

4. **Cálculo del total**
   - Calcula automáticamente el total de todos los gastos registrados y lo muestra en pantalla de manera destacada.
<img width="714" height="1599" alt="image" src="https://github.com/user-attachments/assets/05123192-5a12-4c04-b556-744b8e7fdf88" />

5. **Recordatorios diarios**
   - Configuración de notificaciones diarias para recordar al usuario ingresar sus gastos.
   - Se puede activar o desactivar.
   - Permite seleccionar la **hora exacta** mediante un `TimePicker`.
     <img width="714" height="1599" alt="image" src="https://github.com/user-attachments/assets/7b3e102e-ff37-4521-8e87-c6a84f3651ad" />


6. **Interfaz amigable**
   - Desarrollada con **Jetpack Compose** para una UI moderna y reactiva.
   - Uso de tarjetas (`Card`) para diferenciar secciones.
   - Uso de **dropdowns** para seleccionar categorías.
   - Indicadores visuales claros para acciones de edición y eliminación.

---

## Tecnologías utilizadas

- **Kotlin**: lenguaje principal de desarrollo.
- **Jetpack Compose**: para construir la interfaz de usuario de manera declarativa.
- **Material3**: para estilos modernos y consistentes.
- **Room (ExpenseEntity)**: para el almacenamiento local de los gastos.
- **ViewModel + StateFlow**: para manejar estados y datos de la UI de manera reactiva.
- **Git y GitHub**: control de versiones y repositorio remoto.

---

## Estructura del proyecto

ExpenseTracker/
├── ui/ # Pantallas y componentes de la UI
│ ├── ExpenseScreen.kt # Pantalla principal y gestión de gastos
│ ├── FormularioGasto.kt # Formulario para agregar gastos
│ ├── GastoItem.kt # Item de gasto en la lista
│ └── EditExpenseDialog.kt # Diálogo para editar gastos
├── data/ # Datos y entidades
│ └── local/ExpenseEntity.kt
├── viewmodel/ # Lógica de negocio
│ └── ExpenseViewModel.kt
└── build.gradle


---

## Uso de la aplicación

1. Abrir la aplicación en un emulador o dispositivo Android.
2. En la pantalla principal:
   - Ingresar un nuevo gasto con monto, descripción y categoría.
   - Presionar **Guardar** para registrarlo.
3. Consultar el **historial de gastos**.
4. Editar un gasto haciendo clic en el icono de **editar**.
5. Eliminar un gasto haciendo clic en el icono de **eliminar**.
6. Configurar un **recordatorio diario** activando la opción y seleccionando la hora deseada.

---

## Cómo contribuir

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Sebastian-Betancourt/Taller2_APP.git
Crear una rama para tus cambios:

git checkout -b mi-nueva-funcionalidad
Hacer commit de tus cambios:

git add .
git commit -m "Descripción de lo que hiciste"
Subir la rama a GitHub:

git push origin mi-nueva-funcionalidad
Abrir un Pull Request para revisión.

Capturas de pantalla (Opcional)
Puedes agregar capturas de tu app para mostrar la UI y las funcionalidades.

Autor
Sebastian Betancourt – GitHub
