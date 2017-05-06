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
				<voucher>
					<printCode><xsl:value-of select="orderDetail/bookingId" /></printCode>
					<verifyCode><xsl:value-of select="orderDetail/confirmationId" /></verifyCode>
				</voucher>
				<status>
					<xsl:choose>
						<xsl:when test="orderDetail/statusInd= '0'">1</xsl:when>
						<xsl:when test="orderDetail/statusInd= '1'">4</xsl:when>
						<xsl:otherwise>5</xsl:otherwise>
					</xsl:choose>
				</status>
				<cinemaOrderCode><xsl:value-of select="orderDetail/bookingId" /></cinemaOrderCode>
				<orderItems>
					<xsl:for-each select="orderDetail/seats/seat">
						<orderItem>
							<ticketCode>
								<xsl:value-of select="@ticketNo" />
							</ticketCode>
							<seatCode>
								<xsl:value-of select="@rowId" />_<xsl:value-of select="@columnId" />
							</seatCode>
						</orderItem>
					</xsl:for-each>
				</orderItems>
			</Order>
		</data>
	</xsl:template>
</xsl:stylesheet>