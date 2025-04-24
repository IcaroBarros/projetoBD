package com.aromasdecor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aromasdecor.dao.AdministradorAromasDAO;
import com.aromasdecor.dao.ClienteDAO;
import com.aromasdecor.dao.CompraAvaliaDAO;
import com.aromasdecor.dao.CupomDAO;
import com.aromasdecor.dao.ProdutoDAO;
import com.aromasdecor.model.AdministradorAromas;
import com.aromasdecor.model.Cliente;
import com.aromasdecor.model.Produto;
import com.aromasdecor.model.Cupom;

import java.sql.Date;
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
    public String cadastrarCliente(HttpServletRequest request) {
        String cpf = request.getParameter("cpf");
        String nome = request.getParameter("nome");
        String telefone = request.getParameter("telefone");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        Cliente cliente = new Cliente(cpf, nome, email, telefone, senha);
        clienteDAO.salvar(cliente);
        return "redirect:/";
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
    public String produtos() {
        return "produtos";
    }

    @GetMapping("/adm")
    public String telaInicialAdministracao(Model model) {
        int totalProdutos = produtoDAO.contarProdutos();
        int totalClientes = clienteDAO.contarClientes();
        int totalCupons = cupomDAO.contarCuponsAtivos();
        int totalCompras = compraDAO.contarCompras();
        int totalAvaliacoes = compraDAO.contarAvaliacoes();

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
        String condicoesUso = request.getParameter("condicoesUso");

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
}
