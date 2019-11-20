const { Schema, model } = require("mongoose");

const FaleConoscoSchema = new Schema({
    nome: {
        type: String,
        required: true
    },
    email: {
        type: String,
        required: true
    },
    mensagem: {
        type: String,
    },
}, {
    timestamps: true
});

module.exports = model("FaleConosco", FaleConoscoSchema);