<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/BackTicketResult">
		<BackTicketResult>
			<ResultCode><xsl:value-of select="ResultCode" /></ResultCode>
			<refunded>
				<xsl:choose>
					<xsl:when test="ResultCode='1'">false</xsl:when>
					<xsl:when test="ResultCode='101509'">false</xsl:when>
					<xsl:when test="ResultCode='100500'">false</xsl:when>
					<xsl:otherwise>true</xsl:otherwise>
				</xsl:choose>
			</refunded>
		</BackTicketResult>
	</xsl:template>
</xsl:stylesheet>