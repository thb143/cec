<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/root/data">
		<cinema>
			<halls>	
				<xsl:for-each select="item">
					<hall>
						<code><xsl:value-of select="id" /></code>
						<name><xsl:value-of select="name" /></name>
					    <seatCount><xsl:value-of select="seatNum" /></seatCount>
					</hall>
				</xsl:for-each>
			</halls>		
		</cinema>
	</xsl:template>
</xsl:stylesheet>