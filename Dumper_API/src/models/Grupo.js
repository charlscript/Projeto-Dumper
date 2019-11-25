const { Schema, model } = require("mongoose");

const GrupoSchema = new Schema({
    nome: {
        type: String,
        required: true
    },
    EmailAdmin: {
        type: String,
        required: true
    },
    messages: [{
        mensagem: String,
        nome: String,
        email: String
    }],
    descricao: {
        type: String,
        required: true
    }
}, {
    timestamps: true
});

module.exports = model("Grupo", GrupoSchema);