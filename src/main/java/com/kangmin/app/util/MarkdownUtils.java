package com.kangmin.app.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MarkdownUtils {

    // markdown格式转换成Text格式
    public static String markdownToText(final String markdown) {
        return markdown.replaceAll("<.*?>", "");
    }

    // markdown格式转换成HTML格式
    public static String markdownToHtml(final String markdown) {
        final Parser parser = Parser.builder().build();
        final Node document = parser.parse(markdown);
        final HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    // 增加扩展[标题锚点，表格生成]
    // Markdown转换成HTML
    public static String markdownToHtmlExtensions(final String markdown) {
        //h标题生成id
        final Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
        final List<Extension> tableExtension = List.of(TablesExtension.create());
        final Parser parser = Parser.builder()
                .extensions(tableExtension)
                .build();
        final Node document = parser.parse(markdown);
        final HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    public AttributeProvider create(final AttributeProviderContext context) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        return renderer.render(document);
    }

    /**
     * 处理标签的属性.
     */
    static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(
            final Node node,
            final String tagName,
            final Map<String, String> attributes
        ) {
            // 改变a标签的target属性为_blank
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof TableBlock) {
                attributes.put("class", "ui celled table");
            }
        }
    }
}
