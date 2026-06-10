package com.br.norris.service;

import com.br.norris.entity.Produto;
import com.br.norris.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlProdutoService {

    private final ProdutoRepository produtoRepository;

    public void atualizarUrlsProdutos() {

        try {

            Document sitemap = Jsoup.connect(
                    "https://www.norrisimports.com.br/sitemap/product-1.xml"
            ).get();

            Elements urls = sitemap.select("loc");
            System.out.println("TOTAL DE URLS ENCONTRADAS: " + urls.size());

            int contador = 0;

            for (var loc : urls) {

                if (contador >= 10) {
                    break;
                }

                contador++;

                String url = loc.text();

                try {

                    Document produtoDoc =
                            Jsoup.connect(url).get();

                    String sku = produtoDoc
                            .select("span[itemprop=sku]")
                            .text()
                            .trim();
                    System.out.println("SKU DO SITE: " + sku);

                    if (sku.isEmpty()) {
                        continue;
                    }

                    produtoRepository
                            .findByCodigo(sku)
                            .ifPresent(produto -> {

                                produto.setUrlProduto(url);

                                produtoRepository.save(produto);

                                System.out.println(
                                        "Atualizado: "
                                                + sku
                                                + " -> "
                                                + url
                                );
                            });

                } catch (Exception e) {

                    System.out.println(
                            "Erro ao processar: "
                                    + url
                    );
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}