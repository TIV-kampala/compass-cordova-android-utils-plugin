package org.apache.cordova.plugin

import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CallbackContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import android.util.Log

/**
 * This class echoes a string called from JavaScript.
 */
class Util : CordovaPlugin() {

  private val rsaCipherWrap: RSACipherWrap by lazy { RSACipherWrap() }
  private val aesCipherWrap: AESCipherWrap by lazy { AESCipherWrap() }
  private val preferencesManager: PreferencesManager by lazy { PreferencesManager(context = this.cordova.getActivity().getApplicationContext()) }
  private val clientSecurePayloadProducer: ClientSecurePayloadProducer by lazy { ClientSecurePayloadProducer() }


  override fun execute(
    action: String,
    args: JSONArray,
    callbackContext: CallbackContext
  ): Boolean {

    try {
    
      if (action == "echo") {
        cordova.threadPool.execute {
          val message: String = args.getString(0)
          echo(message, callbackContext)
        }
        return true
      }

      if(action == "generateRsaKeyPair") {
          cordova.threadPool.execute {
            generateRsaKeyPair(callbackContext)
          }
          return true
      }

      if(action == "generateAesKey") {
          cordova.threadPool.execute {
            generateAesKey(callbackContext);
          }
          return true
      }

      if(action == "saveStringData") {
        cordova.threadPool.execute {
          val key: String = args.getString(0)
          val value: String = args.getString(1)
          saveStringData(key, value, callbackContext)
        }
        return true
      }

      if(action == "saveBoolData") {
        cordova.threadPool.execute {
          val key: String = args.getString(0)
          val value: Boolean = args.getString(1) == "true";
          saveBoolData(key, value, callbackContext);
        }
        return true
      }

      if(action == "getStringData") {
        cordova.threadPool.execute {
          val key: String = args.getString(0)
          getStringData(key, callbackContext)
        }
        return true
      }

      if(action == "getBoolData") {
        cordova.threadPool.execute {
          val key: String = args.getString(0)
          getBoolData(key, callbackContext)
        }
        return true
      }

      if(action == "clearData") {
        cordova.threadPool.execute {
          val key: String = args.getString(0)
          clearData(key, callbackContext)
        }
        return true
      }

      if(action == "prepareRequestPayload") {
        cordova.threadPool.execute {
          try {
            val payload: String = args.getString(0)
            prepareRequestPayload(JSONObject(payload), callbackContext)
          } catch (e: Exception) {
            callbackContext.error("Error prepareRequestPayload: ${e.message}")
          }
          
        }
        return true
      }

      if(action == "parseResponsePayload") {
        cordova.threadPool.execute {
          try {
            val payload: String = args.getString(0);
            parseResponsePayload(payload, callbackContext)
          } catch (e: Exception) {
            callbackContext.error("Error parseResponsePayload: ${e.message}")
          }
          
        }
        return true
      }

      return false

    } catch (e: Exception) {
        callbackContext.error("Unexpected error: ${e.message}")
        return false
    }

  }

  private fun echo(
    message: String,
    callbackContext: CallbackContext
  ) {
    if (message.isNotEmpty()) {
      callbackContext.success(message);
    } else {
      callbackContext.error("Expected one non-empty string argument.");
    }
  }

  fun generateRsaKeyPair(callbackContext: CallbackContext){
    val rsaKeyPair = rsaCipherWrap.generateKeyPair()
    val publicKey = CompassEncodedKeySpec.encodeToString(rsaKeyPair.public)

    val resultJson = JSONObject();
    resultJson.put("publicKey", publicKey);

    callbackContext.success(resultJson)
  }

  fun generateAesKey(callbackContext: CallbackContext){
    val aesSecretKey =  aesCipherWrap.generateAESKey()
    val aesStringKey = aesCipherWrap.aesSecreteKeyToString(aesSecretKey)

    val resultJson = JSONObject();
    resultJson.put("aesSecretKey", aesStringKey);

    callbackContext.success(resultJson)
  }

  fun saveStringData(key: String, value: String, callbackContext: CallbackContext){
    preferencesManager.saveStringData(key, value)
    val resultJson = JSONObject();
    resultJson.put("success", true);

    callbackContext.success(resultJson)
  }

  fun saveBoolData(key: String, value: Boolean, callbackContext: CallbackContext){
    preferencesManager.saveBoolData(key, value)
    val resultJson = JSONObject();
    resultJson.put("success", true);

    callbackContext.success(resultJson)
  }

  fun getStringData(key: String, callbackContext: CallbackContext){
    val value = preferencesManager.getStringData(key = key, "")
    val resultJson = JSONObject();
    resultJson.put("data", value);

    callbackContext.success(resultJson)
  }

  fun getBoolData(key: String, callbackContext: CallbackContext){
    val value = preferencesManager.getBoolData(key = key)
    val resultJson = JSONObject();
    resultJson.put("data", value);

    callbackContext.success(resultJson)
  }

  fun clearData(key: String, callbackContext: CallbackContext){
    val value = preferencesManager.clearData(key = key)
    val resultJson = JSONObject();
    resultJson.put("success", value);

    callbackContext.success(resultJson)
  }

  fun prepareRequestPayload(payload: JSONObject, callbackContext: CallbackContext){
    val resultJson = JSONObject();
    
    if(!(payload.has("cmt") || !payload.has("bridgeRaPublicKey"))) {
      resultJson.put("code", 0);
      resultJson.put("message", "Missing  parameter");
      callbackContext.error(resultJson)
    } else {
      val cmt = payload.getString("cmt") ?: ""
      val bridgeRaPublicKey = payload.getString("bridgeRaPublicKey") ?: ""
      Log.d("bridgeRaPublicKey", bridgeRaPublicKey);
      if(bridgeRaPublicKey == "") {
        resultJson.put("code", 0);
        resultJson.put("message", "Empty bridgeRaPublicKey paramater");
        return callbackContext.error(resultJson);
      }
      val response = clientSecurePayloadProducer.prepareRequestPayload(cmt, bridgeRaPublicKey)
      
      resultJson.put("requestData", response);
      callbackContext.success(resultJson)
    }
  }

  fun parseResponsePayload(cmtPayload: String, callbackContext: CallbackContext){
    val response = clientSecurePayloadProducer.parseResponsePayload(cmtPayload)
    val resultJson = JSONObject();
    resultJson.put("responseData", response);

    callbackContext.success(resultJson)
  }


}