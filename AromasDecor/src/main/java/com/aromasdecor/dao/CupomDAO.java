package com.aromasdecor.dao;

import com.aromasdecor.database.Conexao;
import com.aromasdecor.model.Cupom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CupomDAO {

    public void salvar(Cupom cupom) {
        String sql = "INSERT INTO cupom (codigo, dataInicio, dataVencimento, valorDesconto, descricao, condicoesUso) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cupom.getCodigo());
            stmt.setDate(2, cupom.getDataInicio());
            stmt.setDate(3, cupom.getDataVencimento());
            stmt.setDouble(4, cupom.getValorDesconto());
            stmt.setString(5, cupom.getDescricao());
            stmt.setDouble(6, cupom.getCondicoesUso());

            stmt.executeUpdate();
            System.out.println("Cupom salvo com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar cupom: " + e.getMessage());
        }
    }

    public int contarCuponsAtivos() {
        String sql = "SELECT COUNT(*) FROM cupom WHERE dataVencimento >= CURDATE()";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Erro ao contar cupons ativos: " + e.getMessage());
        }

        return 0;
    }
    
    public List<Cupom> getTodosCupons() {
        String sql = "SELECT * FROM cupom";
        List<Cupom> cupons = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cupom cupom = new Cupom(
                    rs.getInt("codigo"),
                    rs.getDate("dataInicio"),
                    rs.getDate("dataVencimento"),
                    rs.getDouble("valorDesconto"),
                    rs.getString("descricao"),
                    rs.getDouble("condicoesUso")
                );
                cupons.add(cupom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cupons;
    }
    
    public void remover(int codigoCupom) {
        String sqlDeletaCompras = "DELETE FROM compra_avalia WHERE codigo_cupom = ?";
        String sqlDeletaCupom = "DELETE FROM cupom WHERE codigo = ?";

        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtCompras = conn.prepareStatement(sqlDeletaCompras);
                PreparedStatement stmtCupom = conn.prepareStatement(sqlDeletaCupom)) {

                stmtCompras.setInt(1, codigoCupom);
                stmtCompras.executeUpdate();

                stmtCupom.setInt(1, codigoCupom);
                stmtCupom.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void atualizarCupom(Cupom cupom) {
        String sql = "UPDATE cupom SET valorDesconto = ?, descricao = ?, condicoesUso = ? WHERE codigo = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, cupom.getValorDesconto());
            stmt.setString(2, cupom.getDescricao());
            stmt.setDouble(3, cupom.getCondicoesUso());
            stmt.setInt(4, cupom.getCodigo());

            stmt.executeUpdate();
            System.out.println("Cupom atualizado com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar cupom: " + e.getMessage());
        }
    }

    public Cupom buscarPorCodigo(int codigo) {
        String sql = "SELECT * FROM cupom WHERE codigo = ?";
        Cupom cupom = null;
        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cupom = new Cupom(
                    rs.getInt("codigo"),
                    rs.getDate("dataInicio"),
                    rs.getDate("dataVencimento"),
                    rs.getDouble("valorDesconto"),
                    rs.getString("descricao"),
                    rs.getDouble("condicoesUso")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cupom;
    }

    
}
