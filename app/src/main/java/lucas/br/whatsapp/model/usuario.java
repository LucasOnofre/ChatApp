package lucas.br.whatsapp.model;

/**
 * Created by Lucas on 13/03/2018.
 */

public class usuario {

    private String nome;
    private String email;
    private String senha;

    public usuario(String nome, String email, String senha) {
        
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
