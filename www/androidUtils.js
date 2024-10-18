const exec = require("cordova/exec");

class AndroidUtils {
  constructor() {}
  async execute(action, args) {
    return new Promise((resolve, reject) => {
      exec(
        (data) => {
          console.log(JSON.stringify(data));
          return resolve(data);
        },
        (err) => {
          // console.log(err);
          return reject(`${err}`);
        },
        "Util",
        action,
        args
      );
    });
  }
  async generateRsaKeyPair() {
    return await this.execute(this.generateRsaKeyPair.name, []);
  }
  async generateAesKey() {
    return await this.execute(this.generateAesKey.name, []);
  }
  async saveStringData(key, value) {
    return await this.execute(this.saveStringData.name, [key, value]);
  }
  async saveBoolData(key, value) {
    return await this.execute(this.saveBoolData.name, [key, value]);
  }
  async getStringData(key) {
    return await this.execute(this.getStringData.name, [key]);
  }
  async getBoolData(key) {
    return await this.execute(this.getBoolData.name, [key]);
  }
  async clearData(key) {
    return await this.execute(this.clearData.name, [key]);
  }
  async prepareRequestPayload(cmtJson, bridgeRAEncPublicKey) {
    return await this.execute(this.prepareRequestPayload.name, [
      JSON.stringify({
        cmt: cmtJson,
        bridgeRaPublicKey: bridgeRAEncPublicKey,
      }),
    ]);
  }
  async parseResponsePayload(encryptedResponsePayload) {
    return await this.execute(this.parseResponsePayload.name, [
      encryptedResponsePayload,
    ]);
  }
}

module.exports = new AndroidUtils();
