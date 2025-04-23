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

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        if (auth.currentUser == null) {
            goToLogin()
            return
        }

        setupNavigation()
        observeUserDetails()

        auth.currentUser?.uid?.let {
            userViewModel.loadUserDetails(it)
        }
    }

    private fun setupNavigation() {
        setSupportActionBar(binding.toolbar)
        drawerLayout = binding.drawerLayout
        navigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_patient_dashboard,
                R.id.nav_doctor_dashboard,
                R.id.nav_admin_dashboard
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun observeUserDetails() {
        userViewModel.user.observe(this, Observer { user ->
            if (user != null) {
                setupDrawerMenuBasedOnRole(UserRole.fromString(user.role))
                updateNavHeader(user.nombre, user.correo)
                navigateToStartFragment(UserRole.fromString(user.role))
            } else {
                Toast.makeText(this, "No se pudieron cargar los datos del usuario.", Toast.LENGTH_LONG).show()
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
        navigationView.menu.clear()
        when (role) {
            UserRole.PATIENT -> navigationView.inflateMenu(R.menu.patient_drawer_menu)
            UserRole.DOCTOR -> navigationView.inflateMenu(R.menu.doctor_drawer_menu)
            UserRole.ADMIN -> navigationView.inflateMenu(R.menu.admin_drawer_menu)
            else -> navigationView.inflateMenu(R.menu.default_drawer_menu)
        }

        navigationView.menu.add(R.id.group_logout, R.id.nav_logout, Menu.NONE, "Cerrar Sesión")
            .setIcon(R.drawable.ic_logout)
    }

    private fun updateNavHeader(name: String?, email: String?) {
        val headerView = navigationView.getHeaderView(0)
        val textViewName = headerView.findViewById<TextView>(R.id.textViewNavHeaderName)
        val textViewEmail = headerView.findViewById<TextView>(R.id.textViewNavHeaderEmail)
        textViewName.text = name ?: "Usuario"
        textViewEmail.text = email ?: ""
    }

    private fun navigateToStartFragment(role: UserRole?) {
        val destination = when (role) {
            UserRole.PATIENT -> R.id.nav_patient_dashboard
            UserRole.DOCTOR -> R.id.nav_doctor_dashboard
            UserRole.ADMIN -> R.id.nav_admin_dashboard
            else -> return
        }

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.main_nav_graph, true)
            .build()

        try {
            navController.navigate(destination, null, navOptions)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error al navegar al destino: ${e.message}")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                signOut()
                return true
            }
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
