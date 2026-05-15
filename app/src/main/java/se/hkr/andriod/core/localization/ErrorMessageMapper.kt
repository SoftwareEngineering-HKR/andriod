package se.hkr.andriod.core.localization

import se.hkr.andriod.R

object ErrorMessageMapper {
    fun map(message: String): Int {
        return when {
            // Room
            message.contains("Could not get all rooms") ->
                R.string.error_rooms_fetch_failed

            message.contains("Failed to create room") ->
                R.string.error_room_create_failed

            message.contains("Failed to update room") ->
                R.string.error_room_update_failed

            message.contains("Failed to delete room") ->
                R.string.error_room_delete_failed

            // Device
            message.contains("Could not get device info") ->
                R.string.error_device_info_failed

            message.contains("Bluetooth scan failed") ->
                R.string.error_bluetooth_scan_failed

            message.contains("Bluetooth connection to device failed") ->
                R.string.error_bluetooth_connect_failed

            message.contains("Failed to update device room") ->
                R.string.error_device_update_room_failed

            message.contains("Failed to update device") ->
                R.string.error_device_update_failed

            message.contains("Failed to delete device") ->
                R.string.error_device_delete_failed

            message.contains("Failed to remove user from device") ->
                R.string.error_device_remove_self_failed

            message.contains("Device could not be contacted") ->
                R.string.error_device_contact_failed

            message.contains("User has no access to requested device") ->
                R.string.error_access_denied

            // Admin / Users
            message.contains("Failed to get users") ->
                R.string.error_users_fetch_failed

            message.contains("could not update the user to desired role") ->
                R.string.error_user_role_update_failed

            message.contains("Error in deleting user") ->
                R.string.error_user_delete_failed

            message.contains("Failed to connect user to device") ->
                R.string.error_device_assign_user_failed

            // Generic
            message.contains("User has no access") ->
                R.string.error_access_denied

            message.contains("Could not") ||
                    message.contains("failed", ignoreCase = true) ->
                R.string.error_generic

             // Fallback
            else -> R.string.error_unknown_error
        }
    }
}
