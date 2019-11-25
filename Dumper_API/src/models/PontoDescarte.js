const { Schema, model } = require("mongoose");

const PontoDescarteSchema = new Schema({
    nome: {
        type: String,
        required: true
    },
    latitude: {
        type: String,
        required: true
    },
    longitude: {
        type: String,
        required: true
    },
    descricao: {
        type: String,
        required: true
    },
    userEmail: {
        type: String,
    },
    validado: Boolean
}, {
    timestamps: true
});

module.exports = model("PontoDescarte", PontoDescarteSchema);