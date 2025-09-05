const { setWorldConstructor } = require('@cucumber/cucumber');

class CustomWorld {
  constructor() {
    this.response = null;   // store HTTP responses for steps
    this.user = null;       // optional, store user info
  }
}

setWorldConstructor(CustomWorld);
