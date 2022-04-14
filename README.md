# Settings Shortcutter
Settings Intent samples

## Notes

- The number inside the parentheses is the API level the intent action was added
  - 0 means Unreleased, eg Tiramisu is 0 as of 2022/04/15
- ACTION_MANAGE_UNKNOWN_APP_SOURCES
  - Apps with "android.permission.REQUEST_INSTALL_PACKAGES" can be toggled
- ACTION_USAGE_ACCESS_SETTINGS
  - "android.permission.PACKAGE_USAGE_STATS" is required to include this app in the list
- ACTION_NFCSHARING_SETTINGS
  - Required Android Beam is deprecated in API level 29
  - See also [isNdefPushEnabled](https://developer.android.com/reference/android/nfc/NfcAdapter#isNdefPushEnabled())
- ACTION_QUICK_ACCESS_WALLET_SETTINGS
  - might be region locked
- ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
  - works for apps with "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" only
- ACTION_SETTINGS_EMBED_DEEP_LINK_ACTIVITY
  - For system or preinstalled apps to show their Activity embedded in Settings app on large screen devices.
- ACTION_STORAGE_VOLUME_ACCESS_SETTINGS
  - This constant was deprecated in API level 29.
  - Use ACTION_APPLICATION_DETAILS_SETTINGS to manage storage permissions for a specific application
- ACTION_WIFI_ADD_NETWORKS
  - doesn't work on emulator
