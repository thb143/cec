<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/data/section">
		<ticketDiscounts>
			<xsl:for-each select="ticket">
				<ticketDiscount>
					<discountPrice><xsl:value-of select="@price" /></discountPrice>
					<discount><xsl:value-of select="@favorableFlg" /></discount>
					<count><xsl:value-of select="@maxTkts" /></count>
					<name><xsl:value-of select="@name" /></name>
				</ticketDiscount>
			</xsl:for-each>
		</ticketDiscounts>
	</xsl:template>
</xsl:stylesheet>