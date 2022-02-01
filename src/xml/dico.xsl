<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : dictionnaire.xsl
    Created on : 4 octobre 2021, 17:17
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
                <title>Dictionnaire</title>
            </head>
            <body>
                <h2>Voici les <xsl:value-of select="count(//tux:mot)"/> mots que vous pourrez retrouver dans le jeu, triés par ordre alphabétique</h2>
                
                <ul>
                    <li>
                        <h4>Mots de niveau 1</h4>
                        <ul>
                            <xsl:apply-templates select="//tux:mot[@niveau='1']">
                                <xsl:sort select="text()"/>
                            </xsl:apply-templates>
                        </ul>
                    </li>
                    
                    <li>
                        <h4>Mots de niveau 2</h4>
                        <ul>
                            <xsl:apply-templates select="//tux:mot[@niveau='2']">
                                <xsl:sort select="text()"/>
                            </xsl:apply-templates>
                        </ul>
                    </li>
                    
                    <li>
                        <h4>Mots de niveau 3</h4>
                        <ul>
                            <xsl:apply-templates select="//tux:mot[@niveau='3']">
                                <xsl:sort select="text()"/>
                            </xsl:apply-templates>
                        </ul>
                    </li>
                    
                    <li>
                        <h4>Mots de niveau 4</h4>
                        <ul>
                            <xsl:apply-templates select="//tux:mot[@niveau='4']">
                                <xsl:sort select="text()"/>
                            </xsl:apply-templates>
                        </ul>
                    </li>
                    
                    <li>
                        <h4>Mots de niveau 5</h4>
                        <ul>
                            <xsl:apply-templates select="//tux:mot[@niveau='5']">
                                <xsl:sort select="text()"/>
                            </xsl:apply-templates>
                        </ul>
                    </li>
                    
                    <li>
                        <h4>Mots de niveau 6 (extrême)</h4>
                        <ul>
                            <xsl:apply-templates select="//tux:mot[@niveau='6']">
                                <xsl:sort select="text()"/>
                            </xsl:apply-templates>
                        </ul>
                    </li>
                </ul>
                <!--
                <h2>FOR EACH</h2>
                <p>
                    <xsl:for-each select="//tux:mot">
                        <xsl:sort select="text()"/> 
                        <xsl:value-of select="text()"/> (niveau <xsl:value-of select="@niveau"/>) <br></br>
                    </xsl:for-each>
                </p>
                -->
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="tux:mot">
        <li>
            <xsl:value-of select="text()"/> <!--(niveau <xsl:value-of select="@niveau"/>)-->
        </li>
    </xsl:template>
    

</xsl:stylesheet>
