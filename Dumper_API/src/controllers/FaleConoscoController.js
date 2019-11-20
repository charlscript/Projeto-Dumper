const FaleConosco = require("../models/FaleConosco");
const Usuario = require("../models/Usuario");

module.exports = {
    async getMessage(request, response) {
        return response.json(await FaleConosco.find({}));
    },

    async delete(req, res) {
        const deleted = await FaleConosco.findByIdAndRemove(req.params.id);

        if (deleted) {
            return res.json({ message: `Mensagem de ID ${req.params.id} excluída` });
        }
    },

    async sendMessage(req, res) {
        const { nome } = req.body;
        const { email } = req.body;
        const { mensagem } = req.body;

        const mensagemExists = await FaleConosco.findOne({
            email: email,
            mensagem: mensagem
        });

        if (mensagemExists) {
            return res.json({ Erro: "Mensagem já enviada" });
        }

        const mailExists = await Usuario.findOne({ email: email });

        if (mailExists) {
            const faleConoscoMensagem = await FaleConosco.create({
                nome: nome,
                mensagem: mensagem,
                email: email
            });
            return res.json({ faleConoscoMensagem });
        } else {
            return res.json({ Erro: "Não existe usuário cadastrado com este email" });
        }
    }
};

//axios = requisição em outras API's
//00:45 vid 02