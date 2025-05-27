package com.aromasdecor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aromasdecor.dao.AdministradorAromasDAO;
import com.aromasdecor.dao.ClienteDAO;
import com.aromasdecor.dao.CompraAvaliaDAO;
import com.aromasdecor.dao.CupomDAO;
import com.aromasdecor.dao.FuncionarioDAO;
import com.aromasdecor.dao.IndicaDAO;
import com.aromasdecor.dao.ProdutoDAO;
import com.aromasdecor.dao.TelefoneDAO;
import com.aromasdecor.model.AdministradorAromas;
import com.aromasdecor.model.Cliente;
import com.aromasdecor.model.CompraAvalia;
import com.aromasdecor.model.Produto;
import com.aromasdecor.model.Telefone;
import com.aromasdecor.model.Cupom;
import com.aromasdecor.model.Funcionario;
import com.aromasdecor.model.Indica;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AromasController {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final AdministradorAromasDAO admDAO = new AdministradorAromasDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final CupomDAO cupomDAO = new CupomDAO();
    private final CompraAvaliaDAO compraDAO = new CompraAvaliaDAO();

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/cadastro_cliente")
    public String cadastrarCliente(
            @RequestParam("cpf") String cpf,
            @RequestParam("nome") String nome,
            @RequestParam("telefone") String[] telefones,
            @RequestParam("email") String email,
            @RequestParam("senha") String senha,
            RedirectAttributes redirectAttrs) {

        Cliente cliente = new Cliente(cpf, nome, telefones[0], email, senha);
        new ClienteDAO().salvar(cliente);

        TelefoneDAO telefoneDAO = new TelefoneDAO();
        for (String tel : telefones) {
            if (tel != null && !tel.trim().isEmpty()) {
                Telefone telefone = new Telefone(cpf, tel);
                telefoneDAO.salvar(telefone);
            }
        }

        redirectAttrs.addFlashAttribute("sucesso", "Cadastro realizado com sucesso!");
        return "home";
    }

    @PostMapping("/login_cliente")
    public String loginCliente(HttpServletRequest request) {
        String cpf = request.getParameter("cpf");
        String senha = request.getParameter("senha");
        boolean autenticado = clienteDAO.verificarLogin(cpf, senha);
        if (autenticado) {
            return "redirect:/";
        }
        return "error";
    }

    @GetMapping("/login_adm")
    public String loginAdministracao() {
        return "loginAdm";
    }

    @PostMapping("/login_adm")
    public String loginAdministracao(HttpServletRequest request) {
        String cpf = request.getParameter("cpf");
        String senha = request.getParameter("senha");
        boolean autenticado = admDAO.verificarAdministrador(cpf, senha);
        if (autenticado) {
            return "redirect:/adm";
        }
        return "error";
    }

    @PostMapping("/cadastro_adm")
    public String cadastrarAdministracao(HttpServletRequest request) {
        String cpf = request.getParameter("cpf");
        String nome = request.getParameter("nome");
        String dataNasc = request.getParameter("data_nasc");
        String telefone = request.getParameter("telefone");
        String rua = request.getParameter("rua");
        String bairro = request.getParameter("bairro");
        String numero = request.getParameter("numero");
        String cidade = request.getParameter("cidade");
        String senha = request.getParameter("senha");
        AdministradorAromas adm = new AdministradorAromas(cpf, nome, dataNasc, telefone, rua, bairro, numero, cidade, senha);
        AdministradorAromasDAO admDAO = new AdministradorAromasDAO();
        admDAO.salvar(adm);
        return "redirect:/adm";
    }

    @GetMapping("/produtos")
    public String produtos(Model model) {
        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> lista = dao.getTodosProdutos();
        model.addAttribute("produtos", lista);
        return "produtos";
    }

    @GetMapping("/produto/compra")
    public String produto(@RequestParam("id") int id, Model model) {
        ProdutoDAO dao = new ProdutoDAO();
        Produto produto = dao.buscarPorId(id);

        if (produto == null) {
            return "redirect:/produtos";
        }

        model.addAttribute("produto", produto);
        return "compraProduto";
    }

    @PostMapping("/produto/compra")
    public String finalizarCompra(@RequestParam("codigoProduto") int codigoProduto,
                                @RequestParam("cpfCliente") String cpf,
                                @RequestParam("codigoCupom") String codigoCupomStr,
                                RedirectAttributes redirectAttributes, Model model) {

        ProdutoDAO produtoDAO = new ProdutoDAO();
        Produto produto = produtoDAO.buscarPorId(codigoProduto);

        Double preco = produto.getPreco();
        String mensagemErroCupom = null;

        CupomDAO cupomDAO = new CupomDAO();
        Cupom cupom = null;

        java.util.Date hoje = new java.util.Date();
                                    
        int codigoCupom = 0;
        if (codigoCupomStr != null && !codigoCupomStr.isEmpty()) {
            try {
                codigoCupom = Integer.parseInt(codigoCupomStr);
                cupom = cupomDAO.buscarPorCodigo(codigoCupom);
                if (cupom == null) {
                    mensagemErroCupom = "Cupom inválido.";
                } else if (preco < cupom.getCondicoesUso()) {
                    mensagemErroCupom = "Preço do produto não atende à condição mínima para este cupom.";
                } else if (cupom.getDataVencimento().before(hoje)) {
                    mensagemErroCupom = "Cupom fora de validade. Tente outro cupom!";
                } else {
                    preco = preco - cupom.getValorDesconto();
                }

            } catch (NumberFormatException e) {
                codigoCupom = 0;
            }
        }

        if (mensagemErroCupom != null) {
            model.addAttribute("produto", produto);
            model.addAttribute("mensagemErroCupom", mensagemErroCupom);
            model.addAttribute("precoFinal", preco);
            return "compraProduto";
        }

        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente cliente = clienteDAO.buscarPorCpf(cpf);

        if (cliente == null) {
            redirectAttributes.addFlashAttribute("erro", "CPF não encontrado. Faça o cadastro primeiro.");
            redirectAttributes.addFlashAttribute("abrirCadastro", true);
            return "redirect:/produto/compra?id=" + codigoProduto;  
        }

        CompraAvalia compra = new CompraAvalia(
            new Random().nextInt(100000),
            Date.valueOf(LocalDate.now()),
            preco,
            "",
            0,
            cpf, 
            codigoProduto,
            codigoCupom
        );

        CompraAvaliaDAO dao = new CompraAvaliaDAO();
        dao.salvar(compra);

        redirectAttributes.addFlashAttribute("compraSucesso", true);
        redirectAttributes.addFlashAttribute("codigoProduto", compra.getCodigoProduto());

        return "redirect:/produto/compra?id=" + compra.getCodigoProduto();
    }

    @GetMapping("/produto/aplicarCupom")
    public String aplicarCupom(
            @RequestParam("codigoProduto") int codigoProduto,
            @RequestParam("codigoCupom") int codigoCupom,
            Model model) {

        ProdutoDAO produtoDAO = new ProdutoDAO();
        Produto produto = produtoDAO.buscarPorId(codigoProduto);

        CupomDAO cupomDAO = new CupomDAO();
        Cupom cupom = cupomDAO.buscarPorCodigo(codigoCupom);

        String mensagemErroCupom = null;
        Double precoFinal = null;

        java.util.Date hoje = new java.util.Date();

        if (cupom == null) {
            mensagemErroCupom = "Cupom inválido.";
        } else if (produto.getPreco() < cupom.getCondicoesUso()) {
            mensagemErroCupom = "Preço do produto não atende à condição mínima para este cupom.";
        } else if (cupom.getDataVencimento().before(hoje)) {
                    mensagemErroCupom = "Cupom fora de validade. Tente outro cupom!";
        } else {
            precoFinal = produto.getPreco() - cupom.getValorDesconto();
        }

        model.addAttribute("produto", produto);
        model.addAttribute("codigoCupom", codigoCupom);
        model.addAttribute("mensagemErroCupom", mensagemErroCupom);
        model.addAttribute("precoFinal", precoFinal);

        return "compraProduto";
    }

    @PostMapping("/produto/avaliar")
    public String avaliarProduto(@RequestParam("codigoProduto") int codigoProduto,
                                @RequestParam("nota") int nota,
                                @RequestParam("avaliacao") String avaliacao,
                                RedirectAttributes redirectAttributes) {

        CompraAvaliaDAO dao = new CompraAvaliaDAO();
        dao.darAvaliacao(codigoProduto, nota, avaliacao);

        redirectAttributes.addFlashAttribute("avaliado", true);
        return "redirect:/produtos";
    }


    @GetMapping("/adm")
    public String telaInicialAdministracao(Model model) {
        int totalProdutos = produtoDAO.contarProdutos();
        int totalClientes = clienteDAO.contarClientes();
        int totalCupons = cupomDAO.contarCuponsAtivos();
        int totalCompras = compraDAO.contarCompras();
        int totalAvaliacoes = compraDAO.contarAvaliacoes();

        Map<Integer, Integer> avaliacoesPorNota = compraDAO.contarAvaliacoesPorNota();
        model.addAttribute("avaliacoesPorNota", avaliacoesPorNota);
        
        int comprasComCupom = compraDAO.contarComprasComCupom();
        int comprasSemCupom = compraDAO.contarComprasSemCupom();
        model.addAttribute("comprasComCupom", comprasComCupom);
        model.addAttribute("comprasSemCupom", comprasSemCupom);

        

        model.addAttribute("totalProdutos", totalProdutos);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalCupons", totalCupons);
        model.addAttribute("totalCompras", totalCompras);
        model.addAttribute("totalAvaliacoes", totalAvaliacoes);

        model.addAttribute("produtos", produtoDAO.getTodosProdutos());
        model.addAttribute("clientes", clienteDAO.getTodosClientes());
        model.addAttribute("cupons", cupomDAO.getTodosCupons());
        model.addAttribute("compras", compraDAO.listarCompras());
        model.addAttribute("avaliacoes", compraDAO.listarAvaliacoes());

        return "administracao";
    }

    @PostMapping("/removerProduto")
    public String removerProduto(@RequestParam("produtoId") int produtoId) {
        produtoDAO.remover(produtoId);
        return "redirect:/adm";
    }
    
    @PostMapping("/editarProduto")
    public String editarProduto(@ModelAttribute Produto produto) {
        produtoDAO.atualizar(produto);
        return "redirect:/adm";
    }
    
    @PostMapping("/adicionarProduto")
    public String adicionarProduto(HttpServletRequest request) {
        String nome = request.getParameter("nome");
        double preco = Double.parseDouble(request.getParameter("preco"));
        String descricao = request.getParameter("descricao");
        int estoque = Integer.parseInt(request.getParameter("estoque"));

        int codigo = new Random().nextInt(9000) + 1000;

        Produto produto = new Produto(
            codigo,
            nome,
            descricao,
            preco,
            estoque,
            new Date(System.currentTimeMillis())           
        );

        ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.salvar(produto);

        return "redirect:/adm";     
    }

    @PostMapping("/removerCliente")
    public String removerCliente(@RequestParam("clienteId") String clienteId) {
        clienteDAO.remover(clienteId);
        return "redirect:/adm";
    }
    
    @PostMapping("/adicionarCupom")
    public String adicionarCupom(HttpServletRequest request) {
        Date dataInicio = Date.valueOf(request.getParameter("dataInicio"));
        Date dataVencimento = Date.valueOf(request.getParameter("dataVencimento"));
        double valorDesconto = Double.parseDouble(request.getParameter("valorDesconto"));
        String descricao = request.getParameter("descricao");
        double condicoesUso = Double.parseDouble(request.getParameter("condicoesUso"));

        int codigo = new Random().nextInt(9000) + 1000;

        Cupom cupom = new Cupom(
            codigo,
            dataInicio,
            dataVencimento,
            valorDesconto,
            descricao,
            condicoesUso
        );

        CupomDAO cupomDAO = new CupomDAO();
        cupomDAO.salvar(cupom);

        return "redirect:/adm";
    }
    
    @PostMapping("/editarCupom")
    public String editarCupom(@ModelAttribute Cupom cupom) {
        cupomDAO.atualizarCupom(cupom);
        return "redirect:/adm";
    }

    @PostMapping("/removerCupom")
    public String removerCupom(@RequestParam("cupomId") int cupomId) {
        cupomDAO.remover(cupomId);
        return "redirect:/adm";
    }

    @PostMapping("/removerCompra")
    public String removerCompra(@RequestParam("compraId") int compraId) {
        compraDAO.remover(compraId);
        return "redirect:/adm";
    }

    @GetMapping("/funcionarios")
    public String listar(Model model) {
        FuncionarioDAO dao = new FuncionarioDAO();
        model.addAttribute("funcionarios", dao.listarTodos());
        model.addAttribute("funcionario", new Funcionario(null, null, null, null, null, null, null, null, null, null));
        return "funcionarios";
    }

    @GetMapping("/funcionarios/editar")
    public String editar(@RequestParam String cpf, Model model) {
        FuncionarioDAO dao = new FuncionarioDAO();
        model.addAttribute("funcionario", dao.buscarPorId(cpf));
        return "funcionarios";
    }

    @PostMapping("/salvarFuncionario")
    public String salvar(@RequestParam("cpf") String cpf,
                         @RequestParam("nome") String nome,
                         @RequestParam("cargo") String cargo,
                         @RequestParam("dataNasc") Date dataNasc,
                         @RequestParam("telefone") String telefone,
                         @RequestParam("cep") String cep,
                         @RequestParam("bairro") String bairro,
                         @RequestParam("numero") String numero,
                         @RequestParam("rua") String rua,
                         @RequestParam("cpfAdministrador") String cpfAdm) {
        FuncionarioDAO dao = new FuncionarioDAO();
        Funcionario f = new Funcionario(cpf, nome, cargo, dataNasc, telefone, cep, bairro, numero, rua, cpfAdm);
        // Se existir, atualiza; senão, salva
        if (dao.buscarPorId(cpf) == null) dao.salvar(f);
        else dao.atualizar(f);
        return "redirect:/funcionarios";
    }

    @GetMapping("/funcionarios/deletar")
    public String deletar(@RequestParam String cpf) {
        FuncionarioDAO dao = new FuncionarioDAO();
        dao.deletar(cpf);
        return "redirect:/funcionarios";
    }

    @PostMapping("/indicarCliente")
    public String indicarCliente(@RequestParam("cpfIndicador") String cpfIndicador,
                                @RequestParam("cpfIndicado") String cpfIndicado,
                                RedirectAttributes redirectAttrs) {

        IndicaDAO indicaDAO = new IndicaDAO();

        ClienteDAO clienteDao = new ClienteDAO();
        if (clienteDao.buscarPorCpf(cpfIndicador) == null) {
            redirectAttrs.addFlashAttribute("erro", "Você não possui cadastro como cliente.");
            return "redirect:/";
        }

        if (cpfIndicador.equals(cpfIndicado)) {
            redirectAttrs.addFlashAttribute("erro", "Você não pode se auto-indicar.");
            return "redirect:/";
        }

        if (indicaDAO.existeIndicacao(cpfIndicador, cpfIndicado)) {
            redirectAttrs.addFlashAttribute("erro", "Você já indicou este cliente.");
            return "redirect:/";
        }

        Indica indica = new Indica(cpfIndicador, cpfIndicado);
        indicaDAO.salvar(indica);

        redirectAttrs.addFlashAttribute("sucesso", "Cliente indicado com sucesso!");
        return "redirect:/";
    }

}
