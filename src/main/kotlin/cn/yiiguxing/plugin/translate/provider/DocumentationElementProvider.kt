package cn.yiiguxing.plugin.translate.provider

import cn.yiiguxing.plugin.translate.util.Plugin
import cn.yiiguxing.plugin.translate.util.findElementOfTypeAt
import com.intellij.lang.Language
import com.intellij.lang.LanguageExtension
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

interface DocumentationElementProvider {

    /**
     * Finds a documentation PSI element at the specified [offset] from the specified [PSI file][psiFile].
     */
    fun findDocumentationElementAt(psiFile: PsiFile, offset: Int): PsiElement?

    fun getDocumentationOwner(documentationElement: PsiElement): PsiElement? {
        return if (documentationElement is PsiDocCommentBase) {
            documentationElement.owner
        } else {
            documentationElement.parent
        }
    }

    private object DefaultDocumentationElementProvider : DocumentationElementProvider {

        override fun findDocumentationElementAt(psiFile: PsiFile, offset: Int): PsiElement? {
            return psiFile.findElementOfTypeAt(offset, PsiDocCommentBase::class.java)
        }

    }

    companion object {
        private val PROVIDERS = LanguageExtension<DocumentationElementProvider>(
            "${Plugin.PLUGIN_ID}.docElementProvider",
            DefaultDocumentationElementProvider
        )

        fun forLanguage(language: Language): DocumentationElementProvider = PROVIDERS.forLanguage(language)
    }

}