<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/reply">
		<reply>
			<code><xsl:value-of select="code" /></code>
			<msg><xsl:value-of select="msg" /></msg>
			<cinema>
			  	<code><xsl:value-of select="cinema/code" /></code>
		        <name><xsl:value-of select="cinema/name" /></name>
		        <xsl:if test="cinema/hallCount != ''">
		        	<hallCount>	<xsl:value-of select="cinema/hallCount" /></hallCount>
		        </xsl:if>
		        <province><xsl:value-of select="cinema/province" /></province>
		        <provinceCode><xsl:value-of select="cinema/provinceCode" /></provinceCode>
		        <city><xsl:value-of select="cinema/city" /></city>
		        <cityCode><xsl:value-of select="cinema/cityCode" /></cityCode>
		        <county><xsl:value-of select="cinema/county" /></county>
		        <countyCode><xsl:value-of select="cinema/countyCode" /></countyCode>
		        <address><xsl:value-of select="cinema/address" /></address>
		        <logo><xsl:value-of select="cinema/logo" /></logo>
				<url><xsl:value-of select="cinema/url" /></url>
		        <tel><xsl:value-of select="cinema/tel" /></tel>
		        <devicePos><xsl:value-of select="cinema/devicePos" /></devicePos>
		        <deviceImg><xsl:value-of select="cinema/deviceImg" /></deviceImg>
		        <xsl:if test="cinema/grade != ''">
		        	<grade><xsl:value-of select="cinema/grade" /></grade>
		        </xsl:if>
		        <intro><xsl:value-of select="cinema/busLine" /></intro>
		        <busLine><xsl:value-of select="cinema/msg" /></busLine>
		        <longitude><xsl:value-of select="cinema/longitude" /></longitude>
		        <latitude><xsl:value-of select="cinema/latitude" /></latitude>
		        <feature><xsl:value-of select="cinema/feature" /></feature>
	        </cinema>
		</reply>
	</xsl:template>
</xsl:stylesheet>