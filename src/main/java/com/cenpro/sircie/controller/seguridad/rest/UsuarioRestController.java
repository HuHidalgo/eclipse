package com.cenpro.sircie.controller.seguridad.rest;

import java.util.List;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cenpro.sircie.aspecto.anotacion.Audit;
import com.cenpro.sircie.aspecto.enumeracion.Accion;
import com.cenpro.sircie.aspecto.enumeracion.Comentario;
import com.cenpro.sircie.aspecto.enumeracion.Dato;
import com.cenpro.sircie.aspecto.enumeracion.Tipo;
import com.cenpro.sircie.model.seguridad.Usuario;
import com.cenpro.sircie.service.IUsuarioService;
import com.cenpro.sircie.utilitario.ConstantesGenerales;
import com.cenpro.sircie.utilitario.ValidatorUtil;
import com.cenpro.sircie.validacion.grupo.accion.IActualizacion;
import com.cenpro.sircie.validacion.grupo.accion.IRegistro;

@Audit(tipo = Tipo.Usu, datos = Dato.Usuario)
@RequestMapping("/seguridad/usuario")
public @RestController class UsuarioRestController
{
    private @Autowired IUsuarioService usuarioService;

    @Audit(accion = Accion.Consulta, comentario = Comentario.ConsultaTodos)
    @GetMapping(params = "accion=buscarTodos")
    public List<Usuario> buscarTodos()
    {
        return usuarioService.buscarTodos();
    }

    @Audit(accion = Accion.Registro, comentario = Comentario.Registro)
    @PostMapping
    public ResponseEntity<?> registrarUsuario(
            @Validated({ IRegistro.class, Default.class }) @RequestBody Usuario usuario,
            Errors error)
    {
        if (error.hasErrors())
        {
            return ResponseEntity.badRequest()
                    .body(ValidatorUtil.obtenerMensajeValidacionError(error));
        }
        usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(usuarioService.buscarPorCodigoUsuario(usuario.getIdUsuario()));
    }

    @Audit(accion = Accion.Actualizacion, comentario = Comentario.Actualizacion)
    @PutMapping
    public ResponseEntity<?> actualizarUsuario(
            @Validated({ IActualizacion.class, Default.class }) @RequestBody Usuario usuario,
            Errors error)
    {
        if (error.hasErrors())
        {
            return ResponseEntity.badRequest()
                    .body(ValidatorUtil.obtenerMensajeValidacionError(error));
        }
        usuarioService.actualizarUsuario(usuario);
        return ResponseEntity.ok(usuarioService.buscarPorCodigoUsuario(usuario.getIdUsuario()));
    }

    @Audit(accion = Accion.Eliminacion, comentario = Comentario.Eliminacion)
    @DeleteMapping
    public ResponseEntity<?> eliminarUsuario(
            @Validated(IActualizacion.class) @RequestBody Usuario usuario, Errors error)
    {
        if (error.hasErrors())
        {
            return ResponseEntity.badRequest()
                    .body(ValidatorUtil.obtenerMensajeValidacionError(error));
        }
        usuarioService.eliminarUsuario(usuario);
        return ResponseEntity.ok(ConstantesGenerales.ELIMINACION_EXITOSA);
    }
}