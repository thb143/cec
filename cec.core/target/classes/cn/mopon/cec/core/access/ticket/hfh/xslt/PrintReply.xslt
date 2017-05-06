<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/data">
		<data>
			<result><xsl:value-of select="@result" /></result>
			<message><xsl:value-of select="@message" /></message>
			<Order>
				<orderItems>
					<xsl:for-each select="orderDetail/seats/seat">
						<orderItem>
							<ticketCode><xsl:value-of select="@ticketNo" /></ticketCode>
							<barCode><xsl:value-of select="@infoCode" /></barCode>
							<seatRow><xsl:value-of select="@rowId" /></seatRow> 
			    			<seatCol><xsl:value-of select="@columnId" /></seatCol>
							<printStatus>
								<xsl:choose>
									<xsl:when test="@printedFlg= 'Y'">1</xsl:when>
									<xsl:otherwise>0</xsl:otherwise>
								</xsl:choose>
							</printStatus>
						</orderItem>
					</xsl:for-each>
				</orderItems>
				<orderSnacks></orderSnacks>
			</Order>
		</data>
	</xsl:template>
</xsl:stylesheet>