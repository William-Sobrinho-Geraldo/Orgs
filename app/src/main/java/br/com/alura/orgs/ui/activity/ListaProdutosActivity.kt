package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.orgs.R
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityListaProdutosActivityBinding
import br.com.alura.orgs.model.Produto
import br.com.alura.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.*

class ListaProdutosActivity : AppCompatActivity() {

    private val adapter = ListaProdutosAdapter(context = this)
    private val binding by lazy {
        ActivityListaProdutosActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()

    }


    override fun onResume() {
        super.onResume()
        val db = AppDatabase.instancia(this)
        val produtoDao = db.produtoDao()
        MainScope().launch {
            val produtos = withContext(Dispatchers.IO){
                produtoDao.buscaTodos()
            }
            adapter.atualiza(produtos)
        }
    }

    private fun configuraFab() {
        val fab = binding.activityListaProdutosFab
        fab.setOnClickListener {
            vaiParaFormularioProduto()
        }
    }

    private fun vaiParaFormularioProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    private fun vaiParaDetalhesProduto(produto: Produto){
        val intent = Intent(this,DetalhesProdutoActivity::class.java)
        intent.putExtra(CHAVE_PRODUTO_ID, produto.id)
        startActivity(intent)
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaProdutosRecyclerView
        recyclerView.adapter = adapter
        adapter.quandoClicaNoItem = {
            vaiParaDetalhesProduto(it)
        }
    }
}