package com.example.recipe.utils

object Constants {
    const val APP_IDENTIFIER = "Recipe"
    const val USER = "user"
    const val IS_LOGGED_IN = "is_logged_in"
    const val IS_ADMIN = "is_admin"
    const val USER_ID = "user_id"
    const val USER_EMAIL = "user_email"

    /** File constants */
    const val NO_MEDIA_FILE = ".nomedia"
    const val RECIPE_PICTURE_DIR = "Recipes"

    /** Date constants */
    const val DATE_FORMAT_FULL = "MMMM d, yyyy" // e.g May 24, 2022
    const val DATE_FORMAT_HYPHEN_DMY = "dd-MM-yyyy" // 10-01-2022
    const val DATE_FORMAT_SPREAD = "yyyy-MM-dd HH:mm:ss" // e.g 2021-01-05 15:00:00

    enum class Status {
        PENDING,
        COMPLETED,
        APPROVED,
        REJECTED
    }
}