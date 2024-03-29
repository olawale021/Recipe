package com.example.recipe.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.recipe.MainActivity
import com.example.recipe.R
import com.example.recipe.db.RecipePreferencesRepository
import com.example.recipe.fragments.login.LoginViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.TimeZone
object UtilsFunctions {
    /**
     * Creates a file directory and a .nomedia file if it does not exist in external files dir
     */
    fun createMediaDirectory(applicationContext: Context, directory: String, root: String) {
        val imageDirectory =
            File(
                Objects.requireNonNull<File>(
                    applicationContext.getExternalFilesDir(
                        root
                    )
                ).absoluteFile,
                directory
            )
        if (!imageDirectory.isDirectory) {
            imageDirectory.mkdirs()
        }
        val noMediaFile = File(imageDirectory.absoluteFile, Constants.NO_MEDIA_FILE)

        if (!noMediaFile.exists()) {
            val outNoMedia = FileOutputStream(noMediaFile)
            outNoMedia.flush()
            outNoMedia.close()
        }
    }
    fun setMenu(activity: FragmentActivity,
                recipePreferencesRepository: RecipePreferencesRepository) {
        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        val navController =
            (activity.supportFragmentManager.findFragmentById(R.id.user_nav_host_fragment) as NavHostFragment).navController
        activity.addMenuProvider(object : MenuProvider {
            @SuppressLint("RestrictedApi", "UseCompatLoadingForDrawables")
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                menu.findItem(R.id.home_item)?.setChecked(true)
                val home = activity.findViewById<ActionMenuItemView>(R.id.home_item)
                home.setBackgroundColor(activity.resources.getColor(R.color.greyBackground))
            }




            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                menuItem.setChecked(true)
//                resetMenuItemColors(activity, menuItem.itemId)
                Log.d("MainActivity", menuItem.itemId.toString())
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        activity.onBackPressedDispatcher.onBackPressed()
                        true
                    }

                    R.id.home_item -> {
                        navController.popBackStack(R.id.homeFragment, false)
                        true
                    }

                    R.id.log_out -> {
                        activity.lifecycleScope.launch {
                            logout(recipePreferencesRepository, activity)
                        }
                        true
                    }
                    R.id.favorite_item -> {
                        navController.navigate(R.id.favoriteFragment)
                        true
                    }

                    else -> false
                }
            }
        }, activity, Lifecycle.State.RESUMED)
    }

    @SuppressLint("RestrictedApi", "UseCompatLoadingForDrawables")
    private fun resetMenuItemColors(activity: FragmentActivity, selectedId: Int) {
        val home = activity.findViewById<ActionMenuItemView>(R.id.home_item)
        val favorite = activity.findViewById<ActionMenuItemView>(R.id.favorite_item)
        if (home.id == selectedId) {
            home.setBackgroundColor(activity.resources.getColor(R.color.greyBackground))
            favorite.setBackgroundColor(activity.resources.getColor(R.color.disabled))
        } else {
            home.setBackgroundColor(activity.resources.getColor(R.color.disabled))
            favorite.setBackgroundColor(activity.resources.getColor(R.color.greyBackground))
        }
    }

    fun setAdminMenu(activity: FragmentActivity, recipePreferencesRepository: RecipePreferencesRepository) {
        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        val navController =
            (activity.supportFragmentManager.findFragmentById(R.id.admin_nav_host_fragment) as NavHostFragment).navController
        activity.addMenuProvider(object : MenuProvider {
            @SuppressLint("RestrictedApi", "UseCompatLoadingForDrawables")
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                menu.findItem(R.id.admin_home_item)?.setChecked(true)
                val adminHome = activity.findViewById<ActionMenuItemView>(R.id.admin_home_item)
       //         adminHome.setBackgroundColor(activity.resources.getColor(R.color.greyBackground))
            }

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                menu.findItem(R.id.admin_home_item)?.setVisible(true)
//                menu.findItem(R.id.order_item)?.setVisible(true)
                menu.findItem(R.id.home_item)?.setVisible(false)
                menu.findItem(R.id.favorite_item)?.setVisible(false)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                menuItem.setChecked(true)
                resetAdminMenuItemColors(activity, menuItem.itemId)
                Log.d("MainActivity", menuItem.itemId.toString())
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        activity.onBackPressedDispatcher.onBackPressed()
                        true
                    }

                    R.id.admin_home_item -> {
                        navController.popBackStack(R.id.adminHomeFragment, false)
                        true
                    }

                    R.id.log_out -> {
                        activity.lifecycleScope.launch {
                            logout(recipePreferencesRepository, activity)
                        }
                        true
                    }

//                    R.id.order_item -> {
//                        navController.navigate(R.id.adminOrdersFragment)
//                        true
//                    }

                    else -> false
                }
            }
                }, activity, Lifecycle.State.RESUMED)
    }

    @SuppressLint("RestrictedApi", "UseCompatLoadingForDrawables")
    private fun resetAdminMenuItemColors(activity: FragmentActivity, selectedId: Int) {
        val adminHome = activity.findViewById<ActionMenuItemView>(R.id.admin_home_item)
//        val order = activity.findViewById<ActionMenuItemView>(R.id.order_item)
        if (adminHome.id == selectedId) {
            adminHome.setBackgroundColor(activity.resources.getColor(R.color.darkBlue))
//            order.setBackgroundColor(activity.resources.getColor(R.color.disabled))
        } else {
            adminHome.setBackgroundColor(activity.resources.getColor(R.color.darkBlue))
//            order.setBackgroundColor(activity.resources.getColor(R.color.darkBlue))
        }
    }

    private suspend fun logout(
        recipePreferencesRepository: RecipePreferencesRepository,
        activity: FragmentActivity
    ) {
        recipePreferencesRepository.clearDataStore()
        activity.finish()
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
    }

    fun displayDatePicker(
        editText: EditText,
        context: Context,
        minDate: Long? = null,
        maxDate: Long? = null,
    ) {
        val cldr = Calendar.getInstance()
        val day = cldr[Calendar.DAY_OF_MONTH]
        val month = cldr[Calendar.MONTH]
        val year = cldr[Calendar.YEAR]

        // date picker dialog
        val picker = DatePickerDialog(
            context,
            { _, selectedYear, monthOfYear, dayOfMonth ->
                // selected month usually starts from 0
                var mm = (monthOfYear + 1).toString()

                if (mm.length == 1) {
                    mm = "0$mm"
                }
                var dd = dayOfMonth.toString()
                if (dd.length == 1) {
                    dd = "0$dd"
                }
                val yearStr = "$dd-$mm-$selectedYear"
                val sdf = SimpleDateFormat(Constants.DATE_FORMAT_HYPHEN_DMY, Locale.getDefault())
                val date = sdf.parse(yearStr)
                val dateStr = date?.let { getDateString(Constants.DATE_FORMAT_FULL, it) }
                editText.setText(dateStr)
            }, year, month, day
        )
        if (minDate != null) {
            picker.datePicker.minDate = cldr.timeInMillis
        }
        if (maxDate != null) {
            picker.datePicker.maxDate = maxDate
        }
        picker.show()
    }

    /**
     * Get Date string in required format
     * Returns today's date string if no date is passed
     * @param keyDateFormat: options can be found in constants class
     */
    fun getDateString(
        keyDateFormat: String,
        date: Date = Date(),
        timeZone: String? = null
    ): String {
        val dateFormat: SimpleDateFormat = when (keyDateFormat) {
            Constants.DATE_FORMAT_HYPHEN_DMY -> {
                SimpleDateFormat(Constants.DATE_FORMAT_HYPHEN_DMY, Locale.getDefault())
            }
            Constants.DATE_FORMAT_FULL -> {
                SimpleDateFormat(Constants.DATE_FORMAT_FULL, Locale.getDefault())
            }
            else -> {
                SimpleDateFormat(Constants.DATE_FORMAT_HYPHEN_DMY, Locale.getDefault())
            }
        }

        if (!timeZone.isNullOrEmpty()) {
            dateFormat.timeZone = TimeZone.getTimeZone(timeZone)
        }
        return dateFormat.format(date)
    }
}