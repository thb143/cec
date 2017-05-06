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
			<ticketOrder>
				<orderItems>
					<xsl:for-each select="tickets/ticket">
						<orderItem>
							<seatCode><xsl:value-of select="seatCode" /></seatCode>
							<barCode><xsl:value-of select="barCode" /></barCode>
							<printStatus>
								<xsl:choose>
									<xsl:when test="printStatus='1'">1</xsl:when>
									<xsl:otherwise>0</xsl:otherwise>
								</xsl:choose>
							</printStatus>
						</orderItem>
					</xsl:for-each>
				</orderItems>
			</ticketOrder>
		</reply>
	</xsl:template>
</xsl:stylesheet>