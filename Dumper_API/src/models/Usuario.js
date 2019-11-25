const { Schema, model } = require("mongoose");
const bcrypt = require("bcryptjs");
SALT_WORK_FACTOR = 10;
const UsuarioSchema = new Schema({
    email: {
        type: String,
        required: true
    },
    grupos: [String]
}, {
    timestamps: true
});

module.exports = model("Usuario", UsuarioSchema);