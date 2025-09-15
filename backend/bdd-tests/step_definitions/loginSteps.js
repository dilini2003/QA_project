import { Given, When, Then } from '@cucumber/cucumber';
import axios from 'axios';
import { expect } from 'chai';

Given('I am a registered user with email {string} and password {string}', async function (email, password) {
  try {
    await axios.post('http://localhost:8080/api/auth/signup', {
      username: "testUser",
      email,
      password
    });
  } catch (err) {
    // ignore if user already exists
  }
});

When('I send a login request with email {string} and password {string}', async function (email, password) {
  try {
    this.response = await axios.post('http://localhost:8080/api/auth/login', { email, password });
  } catch (err) {
    if (err.response) {
      this.response = err.response;
    } else {
      throw new Error(`No response from backend: ${err.message}`);
    }
  }
});

Then('I should receive a success message', function () {
  if (!this.response) throw new Error("No response received");
  expect(this.response.status).to.equal(200);
  // Check for message property in response data
  expect(this.response.data.message || this.response.data).to.contain("Login Successful");
});

Then('I should see an error message {string}', function (message) {
  if (!this.response) throw new Error("No response received");
  expect(this.response.status).to.equal(401);
  // Check for message property in response data
  expect(this.response.data.message || this.response.data).to.equal(message);
});