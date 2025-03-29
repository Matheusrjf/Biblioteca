import java.util.*;

/**
 * Singleton - Gerenciador da Biblioteca
 */
class Biblioteca {
    private static Biblioteca instancia;
    private List<Livro> livros = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    
    private Biblioteca() {}
    
    public static Biblioteca getInstance() {
        if (instancia == null) {
            instancia = new Biblioteca();
        }
        return instancia;
    }
    
    public void adicionarLivro(Livro livro) {
        livros.add(livro);
    }
    
    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
    
    public Livro buscarLivro(String titulo) {
        return livros.stream().filter(l -> l.getTitulo().equalsIgnoreCase(titulo)).findFirst().orElse(null);
    }
}

/**
 * Factory Method - Criação de Livros
 */
abstract class Livro {
    protected String titulo;
    protected String autor;
    
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    
    public abstract void exibirDetalhes();
}

class LivroDigital extends Livro {
    public LivroDigital(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
    }
    
    @Override
    public void exibirDetalhes() {
        System.out.println("[E-Book] " + titulo + " - " + autor);
    }
}

class LivroFisico extends Livro {
    public LivroFisico(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
    }
    
    @Override
    public void exibirDetalhes() {
        System.out.println("[Físico] " + titulo + " - " + autor);
    }
}

/**
 * Factory Method - Criador de livros
 */
class LivroFactory {
    public static Livro criarLivro(String tipo, String titulo, String autor) {
        if ("digital".equalsIgnoreCase(tipo)) {
            return new LivroDigital(titulo, autor);
        } else if ("fisico".equalsIgnoreCase(tipo)) {
            return new LivroFisico(titulo, autor);
        }
        throw new IllegalArgumentException("Tipo de livro desconhecido");
    }
}

/**
 * Decorator - Extensão de funcionalidades dos livros
 */
abstract class LivroDecorator extends Livro {
    protected Livro livro;
    
    public LivroDecorator(Livro livro) {
        this.livro = livro;
    }
    
    @Override
    public String getTitulo() { return livro.getTitulo(); }
    @Override
    public String getAutor() { return livro.getAutor(); }
    @Override
    public abstract void exibirDetalhes();
}

class LivroComResumo extends LivroDecorator {
    public LivroComResumo(Livro livro) {
        super(livro);
    }
    
    @Override
    public void exibirDetalhes() {
        livro.exibirDetalhes();
        System.out.println("[Resumo Disponível]");
    }
}

class LivroComAudio extends LivroDecorator {
    public LivroComAudio(Livro livro) {
        super(livro);
    }
    
    @Override
    public void exibirDetalhes() {
        livro.exibirDetalhes();
        System.out.println("[Audiobook Disponível]");
    }
}

class LivroAcessivel extends LivroDecorator {
    public LivroAcessivel(Livro livro) {
        super(livro);
    }
    
    @Override
    public void exibirDetalhes() {
        livro.exibirDetalhes();
        System.out.println("[Acessibilidade: Texto em alto contraste e suporte a leitor de tela]");
    }
}

/**
 * Proxy - Controle de acesso a livros digitais
 */
class LivroDigitalProxy extends Livro {
    private LivroDigital livroDigital;
    private String titulo;
    private String autor;
    private boolean acessoPermitido;
    
    public LivroDigitalProxy(String titulo, String autor, boolean acessoPermitido) {
        this.titulo = titulo;
        this.autor = autor;
        this.acessoPermitido = acessoPermitido;
    }
    
    @Override
    public void exibirDetalhes() {
        if (acessoPermitido) {
            if (livroDigital == null) {
                livroDigital = new LivroDigital(titulo, autor);
            }
            livroDigital.exibirDetalhes();
        } else {
            System.out.println("Acesso negado ao livro digital: " + titulo);
        }
    }
}

/**
 * Observer - Notificações para usuários
 */
interface Observador {
    void atualizar(String mensagem);
}

class Usuario implements Observador {
    private String nome;
    
    public Usuario(String nome) {
        this.nome = nome;
    }
    
    @Override
    public void atualizar(String mensagem) {
        System.out.println("Notificação para " + nome + ": " + mensagem);
    }
}

class Notificador {
    private List<Observador> observadores = new ArrayList<>();
    
    public void adicionarObservador(Observador obs) {
        observadores.add(obs);
    }
    
    public void notificar(String mensagem) {
        for (Observador obs : observadores) {
            obs.atualizar(mensagem);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Biblioteca biblioteca = Biblioteca.getInstance();
        
        Livro livro1 = LivroFactory.criarLivro("digital", "Design Patterns", "GoF");
        Livro livro2 = LivroFactory.criarLivro("fisico", "Clean Code", "Robert C. Martin");
        Livro livro3 = new LivroComResumo(livro2);
        Livro livro5 = new LivroComAudio(livro1);
        Livro livro6 = new LivroAcessivel(livro1);
        
        Livro livro4 = new LivroDigitalProxy("Refactoring", "Martin Fowler", false);
        
        biblioteca.adicionarLivro(livro1);
        biblioteca.adicionarLivro(livro2);
        biblioteca.adicionarLivro(livro3);
        biblioteca.adicionarLivro(livro4);
        biblioteca.adicionarLivro(livro5);
        biblioteca.adicionarLivro(livro6);
        
        Usuario usuario1 = new Usuario("Alice");
        Notificador notificador = new Notificador();
        notificador.adicionarObservador(usuario1);
        
        notificador.notificar("Novo livro disponível: " + livro1.getTitulo());
        
        livro3.exibirDetalhes();
        livro4.exibirDetalhes();
        livro5.exibirDetalhes();
        livro6.exibirDetalhes();
    }
}

