<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : profil.xsl
    Created on : 4 octobre 2021, 18:34
    Author     : tom
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                version="1.0"
                xmlns:tux="http://myGame/tux">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
        <html>
            <head>
                <title>Profil</title>
                <link href="profil.css" rel="stylesheet" type="text/css"/>
            </head>
            <body>
                <center>
                    <table border="1">
                        <thead>
                            <tr>
                                <td>
                                    <p>
                                        nom : <xsl:value-of select="tux:profil/tux:nom"/>
                                        <br/>
                                        anniversaire : <xsl:value-of select="tux:profil/tux:anniversaire"/>
                                    </p>
                                </td>
                                <td>
                                    <img border="0">
                                        <xsl:attribute name="src">../avatar_icons/<xsl:value-of select="tux:profil/tux:avatar"/></xsl:attribute>
                                        <xsl:attribute name="alt">
                                            <xsl:value-of select="tux:avatar/text()"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="width">150</xsl:attribute>
                                        <xsl:attribute name="height">150</xsl:attribute>
                                    </img>
                                </td>
                            </tr>
                        </thead>
                        <tbody>
                            <xsl:apply-templates select="//tux:parties/tux:partie">
                                <xsl:sort select="@date" order="descending"/>
                            </xsl:apply-templates>
                        </tbody>
                    </table>
                </center>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="//tux:parties/tux:partie">
        <xsl:choose>
            <xsl:when test="tux:temps">
                <tr class="reussite">
                    <td colspan="2">
                        <p>
                            Date : <xsl:value-of select="@date"/>
                            <br/>
                            Mot : <xsl:value-of select="tux:mot"/> 
                            <br/>
                            Niveau : <xsl:value-of select="tux:mot/@niveau"/> 
                            <br/>
                            Trouvé en <xsl:value-of select="tux:temps"/> seconde(s)
                            <br/>
                        </p>
                    </td>
                </tr>
            </xsl:when>
            <xsl:otherwise>
                <tr class="echec">
                    <td colspan="2">
                        <p>
                            Date : <xsl:value-of select="@date"/>
                            <br/>
                            Mot : <xsl:value-of select="tux:mot"/> 
                            <br/>
                            Niveau : <xsl:value-of select="tux:mot/@niveau"/> 
                            <br/>
                            Trouvé à <xsl:value-of select="@trouvé"/>
                            <br/>
                        </p>
                    </td>
                </tr>
            </xsl:otherwise>   
        </xsl:choose>         
    </xsl:template>

</xsl:stylesheet>
