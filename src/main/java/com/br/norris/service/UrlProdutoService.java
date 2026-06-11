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

            ).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0 Safari/537.36")
            .ignoreContentType(true)
            .timeout(30000)
            .get();

            Elements urls = sitemap.select("loc");
            System.out.println("TOTAL DE URLS ENCONTRADAS: " + urls.size());

            int contador = 0;

            for (var loc : urls) {

/*
                if (contador >= 10) {
                    break;
                }
*/

                contador++;

                String url = loc.text();

                try {

                    Document produtoDoc =
                            Jsoup.connect(url)
                                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0 Safari/537.36")
                                    .ignoreContentType(true)
                                    .timeout(30000)
                                    .get();

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