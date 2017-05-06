<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/reply">
		<data>
			<code><xsl:value-of select="code" /></code>
				<msg><xsl:value-of select="msg" /></msg>
			<ticketOrder>	
				<cinemaOrderCode><xsl:value-of select="orderCode" /></cinemaOrderCode>
			</ticketOrder>		
		</data>
	</xsl:template>
</xsl:stylesheet>