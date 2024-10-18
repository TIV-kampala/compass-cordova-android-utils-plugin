---
title: AndroidUtils
description: Generate RSA keys, save data to android SharedPreferences, encrypt & decrypt bridge RA request and response
---
<!--
# license: Licensed to the Apache Software Foundation (ASF) under one
#         or more contributor license agreements.  See the NOTICE file
#         distributed with this work for additional information
#         regarding copyright ownership.  The ASF licenses this file
#         to you under the Apache License, Version 2.0 (the
#         "License"); you may not use this file except in compliance
#         with the License.  You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#         Unless required by applicable law or agreed to in writing,
#         software distributed under the License is distributed on an
#         "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#         KIND, either express or implied.  See the License for the
#         specific language governing permissions and limitations
#         under the License.
-->

# compass-cordova-android-utils-plugin

This plugin defines a global `AndroidUtils` object, which exposes native methods.
Although the object is in the global scope, it is not available until after the `deviceready` event.

```js
document.addEventListener("deviceready", onDeviceReady, false);
function onDeviceReady() {
    console.log(AndroidUtils.generateRsaKeyPair());
}
```

## Installation

    cordova plugin add https://github.com/TIV-kampala/compass-cordova-android-utils-plugin

## Supported Platforms

- Android (cordova >=10.0.0, cordova android version >=9.0.0)

- These plugin has been tested with below configurations
    - MIN_SDK_VERSION (24)
    - SDK_VERSION (34)
    - MIN_BUILD_TOOLS_VERSION (34)
    - GRADLE_VERSION (8.7)
    - JAVA_TARGET_COMPATIBILITY (8)
    - JAVA_SOURCE_COMPATIBILITY (8)
    - KOTLIN_VERSION (1.9.24)

## Methods

- AndroidUtils.generateRsaKeyPair()
- AndroidUtils.generateAesKey()
- AndroidUtils.saveStringData()
- AndroidUtils.saveBoolData()
- AndroidUtils.getStringData()
- AndroidUtils.getBoolData()
- AndroidUtils.clearData()
- AndroidUtils.prepareRequestPayload()
- AndroidUtils.parseResponsePayload()

## AndroidUtils.generateRsaKeyPair()

Generates a pair of RSA keys: a private key stored securely in the Android KeyStore and a public key returned as a base64 string.
Returns a promise which resolves to an object containing the public key in base64 format.

## AndroidUtils.generateAesKey()

Generates an AES key for encryption, which is securely stored in the Android KeyStore.
Returns a promise which resolves to an object containing the aes key in base64 format.

## AndroidUtils.saveStringData(`key: string, value: string`)

Saves a string value to the device's SharedPreferences.
Returns a promise which resolves to an object containing a `success` boolean value i.e `{success: true}`.

## AndroidUtils.saveBoolData(`key: string, value: boolean`)

Saves a boolean value to the device's SharedPreferences.
Returns a promise which resolves to an object containing a `success` boolean value i.e `{success: true}`.

## AndroidUtils.getStringData(`key: string`)

Retrieves a string value from the device's SharedPreferences.
Returns a promise which resolves to an object containing  the data.

## AndroidUtils.getBoolData(`key: string`)

Retrieves a boolean value from the device's SharedPreferences.
Returns a promise which resolves to an object containing  the data.

## AndroidUtils.clearData(`key: string`)

Clears a value from the device's SharedPreferences.
Returns a promise which resolves to an object containing a `success` boolean value i.e `{success: true}`

## AndroidUtils.prepareRequestPayload(`cmtJson: string, bridgeRAEncPublicKey: string`)

Handles encryption of the CMT request payload using the bridgeRAEncPublicKey.
Returns a promise which resolves with a base64 encrypted request payload string.

## AndroidUtils.parseResponsePayload(```encryptedResponsePayload: string```)

Handles decryption of the base64 encrypted CMT response payload string.
Returns a promise which resolves with a decrypted response payload json string.

