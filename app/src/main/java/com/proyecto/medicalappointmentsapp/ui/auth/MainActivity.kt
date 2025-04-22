package com.proyecto.medicalappointmentsapp.ui.auth
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.proyecto.medicalappointmentsapp.R
import com.proyecto.medicalappointmentsapp.data.model.UserRole
import com.proyecto.medicalappointmentsapp.databinding.ActivityMainBinding
import com.proyecto.medicalappointmentsapp.viewmodel.UserViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    // Inyectar ViewModel
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        if (auth.currentUser == null) {
            // Nadie logueado, volver a Login
            goToLogin()
            return // Importante salir del onCreate si no hay usuario
        }

        setupNavigation()
        observeUserDetails()

        // Cargar datos del usuario actual
        auth.currentUser?.uid?.let {
            userViewModel.loadUserDetails(it)
        }
    }

    private fun setupNavigation() {
        setSupportActionBar(binding.toolbar) // Configura la Toolbar

        drawerLayout = binding.drawerLayout
        navigationView = binding.navView

        // Configurar NavController desde NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        // Define los destinos de nivel superior para el menú hamburguesa
        // Estos IDs deben coincidir con los IDs de los menús en R.menu.activity_main_drawer
        // y los IDs de los destinos en tu nav_graph.xml
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_patient_dashboard, R.id.nav_doctor_dashboard, R.id.nav_admin_dashboard // Añade aquí los IDs de tus dashboards
            ), drawerLayout
        )

        // Conecta ActionBar con NavController (para título y botón Up)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Conecta NavigationView con NavController (para manejar clics en el menú)
        navigationView.setupWithNavController(navController)
        navigationView.setNavigationItemSelectedListener(this) // Asegúrate de implementar la interfaz

    }


    // Observa los cambios en los detalles del usuario desde el ViewModel
    private fun observeUserDetails() {
        userViewModel.user.observe(this, Observer { user ->
            if (user != null) {
                Log.d("MainActivity", "Usuario cargado: ${user.nombre}, Rol: ${user.role}")
                // Configurar UI basada en el rol (ej. menú del drawer)
                setupDrawerMenuBasedOnRole(UserRole.fromString(user.role))
                // Actualizar cabecera del Drawer (opcional)
                updateNavHeader(user.nombre, user.correo)
                // Navegar al fragmento inicial correcto según el rol
                navigateToStartFragment(UserRole.fromString(user.role))
            } else {
                // Manejar error o caso donde el usuario no se encuentra en Firestore
                Log.e("MainActivity", "Usuario no encontrado en Firestore o error al cargar.")
                Toast.makeText(this, "No se pudieron cargar los datos del usuario.", Toast.LENGTH_LONG).show()
                // Podrías cerrar sesión aquí si es un error crítico
                // signOut()
            }
        })

        userViewModel.error.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "Error del ViewModel: $it")
            }
        })
    }


    private fun setupDrawerMenuBasedOnRole(role: UserRole?) {
        navigationView.menu.clear() // Limpiar menú anterior
        when (role) {
            UserRole.PATIENT -> navigationView.inflateMenu(R.menu.patient_drawer_menu) // Crea este menu XML
            UserRole.DOCTOR -> navigationView.inflateMenu(R.menu.doctor_drawer_menu) // Crea este menu XML
            UserRole.ADMIN -> navigationView.inflateMenu(R.menu.admin_drawer_menu) // Crea este menu XML
            else -> navigationView.inflateMenu(R.menu.default_drawer_menu) // Menú por defecto o vacío
        }
        // Añadir opción común de Logout a todos los menús
        navigationView.menu.add(R.id.group_logout, R.id.nav_logout, Menu.NONE, "Cerrar Sesión")
            .setIcon(R.drawable.ic_logout) // Asegúrate de tener este icono
    }

    private fun updateNavHeader(name: String?, email: String?) {
        val headerView = navigationView.getHeaderView(0) // Obtener la cabecera
        val textViewName = headerView.findViewById<TextView>(R.id.textViewNavHeaderName) // Asegúrate de tener estos IDs en nav_header_main.xml
        val textViewEmail = headerView.findViewById<TextView>(R.id.textViewNavHeaderEmail)
        textViewName?.text = name ?: "Usuario"
        textViewEmail?.text = email ?: ""
    }

    private fun navigateToStartFragment(role: UserRole?) {
        // Asegúrate de que el gráfico de navegación está cargado
        // y el navController está listo antes de intentar navegar
        if (navController.currentDestination == null) {
            // Si aún no hay destino, esperamos un poco (no es ideal, mejor manejar estados)
            // o usamos post para ejecutar después del layout
            binding.root.post { navigateToStartFragment(role) }
            return
        }

        val startDestinationId = when (role) {
            UserRole.PATIENT -> R.id.nav_patient_dashboard // ID del fragmento en tu nav_graph
            UserRole.DOCTOR -> R.id.nav_doctor_dashboard  // ID del fragmento en tu nav_graph
            UserRole.ADMIN -> R.id.nav_admin_dashboard   // ID del fragmento en tu nav_graph
            else -> return // Un fragmento por defecto o de error
        }

        // Evitar navegar si ya estamos en el destino correcto
        // O usar popUpTo para limpiar el backstack si es necesario
        // Opciones de navegación para limpiar el stack y evitar volver atrás al fragmento "incorrecto"
        val navOptions = NavOptions.Builder()
            .setPopUpTo(navController.graph.startDestinationId, true) // Limpia hasta el inicio del grafo
            .build()

        try {
            navController.navigate(startDestinationId, null, navOptions)
        } catch (e: IllegalArgumentException) {
            Log.e("MainActivity", "Error al navegar al destino inicial $startDestinationId. ¿Existe en el grafo?", e)
            // Considera navegar a una pantalla de error o cerrar sesión
        }
    }


    // Maneja la selección de ítems en el Navigation Drawer
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                signOut()
                return true
            }
            // Aquí puedes manejar otros ítems comunes si los tienes fuera del NavController,
            // pero generalmente setupWithNavController lo maneja por ti para los destinos del grafo.
            // Ejemplo:
            // R.id.nav_profile -> { navController.navigate(R.id.nav_profile_fragment) }
        }

        // Cierra el drawer después de la selección (si no es manejado por NavController)
        // drawerLayout.closeDrawer(GravityCompat.START)
        // Devuelve false si el NavController debe manejar la navegación, o true si ya lo hiciste tú
        // return true -> indica que ya manejaste el evento.
        // return false -> deja que otras componentes (como NavController) lo manejen.
        // setupWithNavController se encarga, así que podemos dejar que continúe
        // Pero si añadimos manualmente como el Logout, debemos devolver true.
        return false // Dejar que NavController maneje la navegación principal
    }

    // Maneja el botón "Up" (flecha atrás) en la ActionBar
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Maneja el botón físico "Atrás" para cerrar el drawer si está abierto
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed() // Comportamiento normal del botón atrás
        }
    }


    private fun signOut() {
        auth.signOut()
        goToLogin()
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        }
}