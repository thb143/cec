<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/root/data">
		<ticketOrder>
			<orderItems>
				<xsl:for-each select="ticketInfo/item">
					<orderItem>
						<ticketCode><xsl:value-of select="no" /></ticketCode>
						<barCode><xsl:value-of select="qrCode" /></barCode>
						<printStatus>
							<xsl:choose>
								<xsl:when test="printed='1'">1</xsl:when>
								<xsl:otherwise>0</xsl:otherwise>
							</xsl:choose>
						</printStatus>
					</orderItem>
				</xsl:for-each>
			</orderItems>
			<orderSnacks></orderSnacks>
		</ticketOrder>	
	</xsl:template>
</xsl:stylesheet>