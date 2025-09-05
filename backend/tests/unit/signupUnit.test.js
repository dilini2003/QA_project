import { expect } from "chai";
import { validateEmail } from "../../src/utils/validateEmail.js";

describe("Email Validation", () => {
  it("should return true for valid email", () => {
    expect(validateEmail("user@example.com")).to.be.true;
  });

  it("should return false for invalid email", () => {
    expect(validateEmail("invalid-email")).to.be.false;
  });
});
