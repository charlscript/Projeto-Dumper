const PontoDescarte = require("../models/PontoDescarte");
const Usuario = require("../models/Usuario");

module.exports = {
    async index(request, response) {
        return response.json(await PontoDescarte.find({}));
    },

    async delete(req, res) {
        const deleted = await PontoDescarte.findByIdAndRemove(req.params.id);

        if (deleted) {
            return res.json({ message: `Ponto de ID ${req.params.id} excluído` });
        }
    },

    async validate(req, res) {
        const ponto = await PontoDescarte.findByIdAndUpdate(req.params.id, { validado: true }, { new: true });
        return res.json(ponto);
    },

    async store(req, res) {
        const { nome } = req.body;
        const { latitude } = req.body;
        const { longitude } = req.body;
        const { descricao } = req.body;
        const { validado } = req.body;
        const { userEmail } = req.body;

        const pontoExists = await PontoDescarte.findOne({
            latitude: latitude,
            longitude: longitude,
            nome: nome
        });

        if (pontoExists) {
            return res.json({ Erro: "Este ponto já está cadastrado" });
        }

        return res.json(
            await PontoDescarte.create({
                nome: nome,
                latitude: latitude,
                longitude: longitude,
                descricao: descricao,
                userEmail: userEmail,
                validado: validado
            })
        );
    }
};

//axios = requisição em outras API's
//00:45 vid 02