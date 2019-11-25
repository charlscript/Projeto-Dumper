const Usuario = require("../models/Usuario");
const Grupo = require("../models/Grupo");

module.exports = {
    async index(request, response) {
        return response.json(await Usuario.find({}));
    },

    async findUser(req, res) {
        const { email } = req.body
        const user = await Usuario.findOne({ email: email })
        const records = await Grupo.find().where('_id').in(user.grupos).exec()
        return res.json(records)
    },

    async delete(req, res) {
        const deleted = await Usuario.findByIdAndRemove(req.params.id);
        if (deleted) {
            return res.json({
                message: `Usuario de ID ${req.params.id} excluído`
            });
        }
    },

    async store(req, res) {
        const { email } = req.body;
        const { grupos } = req.body;

        const userExists = await Usuario.findOne({
            email: email
        });

        if (userExists) {
            userExists.grupos.push(grupos);
            userExists.save();
            return res.json(userExists);
        }

        return res.json(
            await Usuario.create({
                email: email,
                grupos: grupos
            })
        );
    },


};