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
			<halls>
				<xsl:for-each select="halls/hall">
					<hall>
						<code><xsl:value-of select="code" /></code>
						<name><xsl:value-of select="name" /></name>
						<seatCount><xsl:value-of select="seatCount" /></seatCount>
					</hall>
				</xsl:for-each>
			</halls>
		</data>
	</xsl:template>
</xsl:stylesheet>