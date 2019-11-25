const Grupo = require("../models/Grupo");

module.exports = {
    async index(request, response) {
        return response.json(await Grupo.find({}));
    },

    async delete(req, res) {
        const deleted = await Grupo.findByIdAndRemove(req.params.id);

        if (deleted) {
            return res.json({ message: `Grupo de ID ${req.params.id} excluído` });
        }
    },

    async sendMessage(req, res) {
        const { message } = req.body;
        const { id } = req.body;
        console.log(req.body)
        const grupo = await Grupo.findById(id)
        grupo.messages.push(message)
        grupo.save()
        return res.json(grupo)
    },
    async meusGrupos(req, res) {
        const { email } = req.body;
        return res.json(await Grupo.find({
            EmailAdmin: email,
        }))
    },

    async find(req, res) {
        const { id } = req.body;
        return res.json(await Grupo.findById(id))
    },

    async store(req, res) {
        const { nome } = req.body;
        const { EmailAdmin } = req.body;
        const { messages } = req.body;
        const { descricao } = req.body;
        console.log(nome, EmailAdmin, descricao)
        return res.json(
            await Grupo.create({
                nome: nome,
                EmailAdmin: EmailAdmin,
                messages: messages,
                descricao: descricao
            })
        );
    }
};